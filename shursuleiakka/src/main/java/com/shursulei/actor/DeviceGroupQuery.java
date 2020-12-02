package com.shursulei.actor;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.remote.WireFormats;
import scala.concurrent.duration.FiniteDuration;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * TODO * @version 1.0 * @author shursulei * @date 2020/11/30 22:29
 * 查询设备组
 * 它有一个可用的温度：Temperature。
 * 它已经响应，但还没有可用的温度：TemperatureNotAvailable。
 * 它在响应之前已停止：DeviceNotAvailable。
 * 它在最后期限之前没有响应：DeviceTimedOut
 */
public class DeviceGroupQuery extends AbstractActor {
    public static final class CollectionTimeout {
    }

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    final Map<ActorRef, String> actorToDeviceId;
    final long requestId;
    final ActorRef requester;

    Cancellable queryTimeoutTimer;

    public DeviceGroupQuery(Map<ActorRef, String> actorToDeviceId, long requestId, ActorRef requester, FiniteDuration timeout) {
        this.actorToDeviceId = actorToDeviceId;
        this.requestId = requestId;
        this.requester = requester;

        queryTimeoutTimer = (Cancellable) getContext().getSystem().scheduler().
              scheduleOnce(timeout, getSelf(), new CollectionTimeout(), getContext().dispatcher(), getSelf());
    }

    public static Props props(Map<ActorRef, String> actorToDeviceId, long requestId, ActorRef requester, FiniteDuration timeout) {
        return Props.create(DeviceGroupQuery.class, () -> new DeviceGroupQuery(actorToDeviceId, requestId, requester, timeout));
    }

    @Override
    public void preStart() {
        for (ActorRef deviceActor : actorToDeviceId.keySet()) {
            getContext().watch(deviceActor);
            deviceActor.tell(new Device.ReadTemperature(0L), getSelf());
        }
    }

    /**
     * 取消定时器
     */
    @Override
    public void postStop() {
        queryTimeoutTimer.cancel();
    }

    @Override
    public Receive createReceive() {
        return waitingForReplies(new HashMap<>(), actorToDeviceId.keySet());
    }

    /**
     * 已收到响应的Map；
     * 我们还在等待 Actor 响应的Set。
     * @param repliesSoFar
     * @param stillWaiting
     * @return
     */
    public Receive waitingForReplies(
            Map<String, DeviceGroup.TemperatureReading> repliesSoFar,
            Set<ActorRef> stillWaiting) {
        return receiveBuilder()
                .match(Device.RespondTemperature.class, r -> {
                    ActorRef deviceActor = getSender();
                    DeviceGroup.TemperatureReading reading = r.value
                            .map(v -> (DeviceGroup.TemperatureReading) new DeviceGroup.Temperature(v))
                            .orElse(DeviceGroup.TemperatureNotAvailable.INSTANCE);
                    receivedResponse(deviceActor, reading, stillWaiting, repliesSoFar);
                })
                .match(Terminated.class, t -> {
                    receivedResponse(t.getActor(), DeviceGroup.DeviceNotAvailable.INSTANCE, stillWaiting, repliesSoFar);
                })
                .match(CollectionTimeout.class, t -> {
                    Map<String, DeviceGroup.TemperatureReading> replies = new HashMap<>(repliesSoFar);
                    for (ActorRef deviceActor : stillWaiting) {
                        String deviceId = actorToDeviceId.get(deviceActor);
                        replies.put(deviceId, DeviceGroup.DeviceTimedOut.INSTANCE);
                    }
                    requester.tell(new DeviceGroup.RespondAllTemperatures(requestId, replies), getSelf());
                    getContext().stop(getSelf());
                })
                .build();
    }
    public void receivedResponse(ActorRef deviceActor,
                                 DeviceGroup.TemperatureReading reading,
                                 Set<ActorRef> stillWaiting,
                                 Map<String, DeviceGroup.TemperatureReading> repliesSoFar) {
        getContext().unwatch(deviceActor);
        String deviceId = actorToDeviceId.get(deviceActor);

        Set<ActorRef> newStillWaiting = new HashSet<>(stillWaiting);
        newStillWaiting.remove(deviceActor);

        Map<String, DeviceGroup.TemperatureReading> newRepliesSoFar = new HashMap<>(repliesSoFar);
        newRepliesSoFar.put(deviceId, reading);
        if (newStillWaiting.isEmpty()) {
            requester.tell(new DeviceGroup.RespondAllTemperatures(requestId, newRepliesSoFar), getSelf());
            getContext().stop(getSelf());
        } else {
            getContext().become(waitingForReplies(newRepliesSoFar, newStillWaiting));
        }
    }
}
