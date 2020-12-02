package com.shursulei.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.FiniteDuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DeviceGroup extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    final String groupId;
    public DeviceGroup(String groupId) {
        this.groupId = groupId;
    }

    public static Props props(String groupId) {
        return Props.create(DeviceGroup.class, () -> new DeviceGroup(groupId));
    }
    public static final class RequestDeviceList {
        final long requestId;

        public long getRequestId() {
            return requestId;
        }

        public RequestDeviceList(long requestId) {
            this.requestId = requestId;
        }
    }
    /**
     *获取设备集合
     */
    public static final class ReplyDeviceList {
        final long requestId;
        final Set<String> ids;

        public long getRequestId() {
            return requestId;
        }

        public Set<String> getIds() {
            return ids;
        }

        public ReplyDeviceList(long requestId, Set<String> ids) {
            this.requestId = requestId;
            this.ids = ids;
        }
    }
    /**
     * 设备id->actorRef
     */
    final Map<String, ActorRef> deviceIdToActor = new HashMap<>();
    /**
     * actorRef->设备id
     */
    final Map<ActorRef, String> actorToDeviceId = new HashMap<>();
    @Override
    public void preStart() {
        log.info("DeviceGroup {} started", groupId);
    }

    @Override
    public void postStop() {
        log.info("DeviceGroup {} stopped", groupId);
    }


    private void onTrackDevice(DeviceManager.RequestTrackDevice trackMsg) {
        if (this.groupId.equals(trackMsg.groupId)) {
            ActorRef deviceActor = deviceIdToActor.get(trackMsg.deviceId);
            if (deviceActor != null) {
                deviceActor.forward(trackMsg, getContext());
            } else {
                log.info("Creating device actor for {}", trackMsg.deviceId);
                deviceActor = getContext().actorOf(Device.props(groupId, trackMsg.deviceId), "device-" + trackMsg.deviceId);
                getContext().watch(deviceActor);
                actorToDeviceId.put(deviceActor, trackMsg.deviceId);
                deviceIdToActor.put(trackMsg.deviceId, deviceActor);
                deviceActor.forward(trackMsg, getContext());
        }
        } else {
            log.warning(
                    "Ignoring TrackDevice request for {}. This actor is responsible for {}.",
                    groupId, this.groupId
            );
        }
    }
    private void onTerminated(Terminated t) {
        ActorRef deviceActor = t.getActor();
        String deviceId = actorToDeviceId.get(deviceActor);
        log.info("Device actor for {} has been terminated", deviceId);
        actorToDeviceId.remove(deviceActor);
        deviceIdToActor.remove(deviceId);
    }
    private void onDeviceList(RequestDeviceList r) {
        getSender().tell(new ReplyDeviceList(r.requestId, deviceIdToActor.keySet()), getSelf());
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DeviceManager.RequestTrackDevice.class, this::onTrackDevice)
                .match(RequestDeviceList.class, this::onDeviceList)
                .match(Terminated.class, this::onTerminated)
                .match(RequestAllTemperatures.class, this::onAllTemperatures)
                .build();
    }

    public static final class RequestAllTemperatures {
        final long requestId;

        public RequestAllTemperatures(long requestId) {
            this.requestId = requestId;
        }
    }

    public static final class RespondAllTemperatures {
        final long requestId;
        final Map<String, TemperatureReading> temperatures;

        public long getRequestId() {
            return requestId;
        }

        public Map<String, TemperatureReading> getTemperatures() {
            return temperatures;
        }

        public RespondAllTemperatures(long requestId, Map<String, TemperatureReading> temperatures) {
            this.requestId = requestId;
            this.temperatures = temperatures;
        }
    }

    public static interface TemperatureReading {
    }

    /**
     * 返回有一个可用的温度
     */
    public static final class Temperature implements TemperatureReading {
        public final double value;

        public Temperature(double value) {
            this.value = value;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Temperature that = (Temperature) o;

            return Double.compare(that.value, value) == 0;
        }

        @Override
        public int hashCode() {
            long temp = Double.doubleToLongBits(value);
            return (int) (temp ^ (temp >>> 32));
        }

        @Override
        public String toString() {
            return "Temperature{" +
                    "value=" + value +
                    '}';
        }
    }

    /**
     * 已经响应，但还没有可用的温度
     */
    public enum TemperatureNotAvailable implements TemperatureReading {
        INSTANCE
    }

    /**
     * 在响应之前已停止
     */
    public enum DeviceNotAvailable implements TemperatureReading {
        INSTANCE
    }

    /**
     * 在最后期限之前没有响应
     */
    public enum DeviceTimedOut implements TemperatureReading {
        INSTANCE
    }
    private void onAllTemperatures(RequestAllTemperatures r) {
        // since Java collections are mutable, we want to avoid sharing them between actors (since multiple Actors (threads)
        // modifying the same mutable data-structure is not safe), and perform a defensive copy of the mutable map:
        //
        // Feel free to use your favourite immutable data-structures library with Akka in Java applications!
        Map<ActorRef, String> actorToDeviceIdCopy = new HashMap<>(this.actorToDeviceId);

        getContext().actorOf(DeviceGroupQuery.props(
                actorToDeviceIdCopy, r.requestId, getSender(), new FiniteDuration(3, TimeUnit.SECONDS)));
    }
}
