package com.shursulei.akkatest;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.testkit.javadsl.TestKit;
import com.shursulei.actor.Device;
import com.shursulei.actor.DeviceGroup;
import com.shursulei.actor.DeviceGroupQuery;
import com.shursulei.actor.DeviceManager;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import scala.concurrent.duration.FiniteDuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DeviceTest {
        @ClassRule
        public static final TestKitJunitResource testKit = new TestKitJunitResource();
//        //#definition
        ActorSystem system = ActorSystem.create("devicesystem");
        @Test
        public void testReplyWithEmptyReadingIfNoTemperatureIsKnown() {
                TestKit probe = new TestKit(system);
                ActorRef deviceActor = system.actorOf(Device.props("group", "device"));
                deviceActor.tell(new Device.ReadTemperature(42L), probe.getRef());
                Device.RespondTemperature response = probe.expectMsgClass(Device.RespondTemperature.class);
                System.out.println(response.getRequestId());
//                assertEquals(42L, response.getRequestId());
                assertEquals(Optional.empty(), response.getValue());
        }

        @Test
        public void testReplyWithLatestTemperatureReading() {
                TestKit probe = new TestKit(system);
                ActorRef deviceActor = system.actorOf(Device.props("group", "device"));

                deviceActor.tell(new Device.RecordTemperature(1L, 24.0), probe.getRef());
                assertEquals(1L, probe.expectMsgClass(Device.TemperatureRecorded.class).getRequestId());

                deviceActor.tell(new Device.ReadTemperature(2L), probe.getRef());
                Device.RespondTemperature response1 = probe.expectMsgClass(Device.RespondTemperature.class);
                assertEquals(2L, response1.getRequestId());
                assertEquals(Optional.of(24.0), response1.getValue());

                deviceActor.tell(new Device.RecordTemperature(3L, 55.0), probe.getRef());
                assertEquals(3L, probe.expectMsgClass(Device.TemperatureRecorded.class).getRequestId());

                deviceActor.tell(new Device.ReadTemperature(4L), probe.getRef());
                Device.RespondTemperature response2 = probe.expectMsgClass(Device.RespondTemperature.class);
                assertEquals(4L, response2.getRequestId());
                assertEquals(Optional.of(55.0), response2.getValue());
        }

        @Test
        public void testReplyToRegistrationRequests() {
                TestKit probe = new TestKit(system);
                ActorRef deviceActor = system.actorOf(Device.props("group", "device"));

                deviceActor.tell(new DeviceManager.RequestTrackDevice("group", "device"), probe.getRef());
                probe.expectMsgClass(DeviceManager.DeviceRegistered.class);
                assertEquals(deviceActor, probe.getLastSender());
        }
        @Test
        public void testIgnoreWrongRegistrationRequests() {
                TestKit probe = new TestKit(system);
                ActorRef deviceActor = system.actorOf(Device.props("group", "device"));

                deviceActor.tell(new DeviceManager.RequestTrackDevice("wrongGroup", "device"), probe.getRef());
                probe.expectNoMessage();

                deviceActor.tell(new DeviceManager.RequestTrackDevice("group", "wrongDevice"), probe.getRef());
                probe.expectNoMessage();
        }
        @Test
        public void testRegisterDeviceActor() {
                TestKit probe = new TestKit(system);
                ActorRef groupActor = system.actorOf(DeviceGroup.props("group"));

                groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device1"), probe.getRef());
                probe.expectMsgClass(DeviceManager.DeviceRegistered.class);
                ActorRef deviceActor1 = probe.getLastSender();

                groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device2"), probe.getRef());
                probe.expectMsgClass(DeviceManager.DeviceRegistered.class);
                ActorRef deviceActor2 = probe.getLastSender();
                assertNotEquals(deviceActor1, deviceActor2);

                // Check that the device actors are working
                deviceActor1.tell(new Device.RecordTemperature(0L, 1.0), probe.getRef());
                assertEquals(0L, probe.expectMsgClass(Device.TemperatureRecorded.class).getRequestId());
                deviceActor2.tell(new Device.RecordTemperature(1L, 2.0), probe.getRef());
                assertEquals(1L, probe.expectMsgClass(Device.TemperatureRecorded.class).getRequestId());
        }

        @Test
        public void testIgnoreRequestsForWrongGroupId() {
                TestKit probe = new TestKit(system);
                ActorRef groupActor = system.actorOf(DeviceGroup.props("group"));

                groupActor.tell(new DeviceManager.RequestTrackDevice("wrongGroup", "device1"), probe.getRef());
                probe.expectNoMessage();
        }
        @Test
        public void testReturnSameActorForSameDeviceId() {
                TestKit probe = new TestKit(system);
                ActorRef groupActor = system.actorOf(DeviceGroup.props("group"));

                groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device1"), probe.getRef());
                probe.expectMsgClass(DeviceManager.DeviceRegistered.class);
                ActorRef deviceActor1 = probe.getLastSender();

                groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device1"), probe.getRef());
                probe.expectMsgClass(DeviceManager.DeviceRegistered.class);
                ActorRef deviceActor2 = probe.getLastSender();
                assertEquals(deviceActor1, deviceActor2);
                System.out.println(groupActor);
                System.out.println(deviceActor1);
                System.out.println(deviceActor2);
        }
        @Test
        public void testListActiveDevices() {
                TestKit probe = new TestKit(system);
                ActorRef groupActor = system.actorOf(DeviceGroup.props("group"));

                groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device1"), probe.getRef());
                probe.expectMsgClass(DeviceManager.DeviceRegistered.class);

                groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device2"), probe.getRef());
                probe.expectMsgClass(DeviceManager.DeviceRegistered.class);

                groupActor.tell(new DeviceGroup.RequestDeviceList(0L), probe.getRef());
                DeviceGroup.ReplyDeviceList reply = probe.expectMsgClass(DeviceGroup.ReplyDeviceList.class);
                assertEquals(0L, reply.getRequestId());
                assertEquals(Stream.of("device1", "device2").collect(Collectors.toSet()), reply.getIds());
        }

        @Test
        public void testListActiveDevicesAfterOneShutsDown() {
                TestKit probe = new TestKit(system);
                ActorRef groupActor = system.actorOf(DeviceGroup.props("group"));

                groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device1"), probe.getRef());
                probe.expectMsgClass(DeviceManager.DeviceRegistered.class);
                ActorRef toShutDown = probe.getLastSender();

                groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device2"), probe.getRef());
                probe.expectMsgClass(DeviceManager.DeviceRegistered.class);

                groupActor.tell(new DeviceGroup.RequestDeviceList(0L), probe.getRef());
                DeviceGroup.ReplyDeviceList reply = probe.expectMsgClass(DeviceGroup.ReplyDeviceList.class);
                assertEquals(0L, reply.getRequestId());
                assertEquals(Stream.of("device1", "device2").collect(Collectors.toSet()), reply.getIds());

                probe.watch(toShutDown);
                toShutDown.tell(PoisonPill.getInstance(), ActorRef.noSender());
                probe.expectTerminated(toShutDown);

                // using awaitAssert to retry because it might take longer for the groupActor
                // to see the Terminated, that order is undefined
                probe.awaitAssert(() -> {
                        groupActor.tell(new DeviceGroup.RequestDeviceList(1L), probe.getRef());
                        DeviceGroup.ReplyDeviceList r =
                                probe.expectMsgClass(DeviceGroup.ReplyDeviceList.class);
                        assertEquals(1L, r.getRequestId());
                        assertEquals(Stream.of("device2").collect(Collectors.toSet()), r.getIds());
                        return null;
                });
        }
        @Test
        public void testReturnTemperatureValueForWorkingDevices() {
                TestKit requester = new TestKit(system);

                TestKit device1 = new TestKit(system);
                TestKit device2 = new TestKit(system);

                Map<ActorRef, String> actorToDeviceId = new HashMap<>();
                actorToDeviceId.put(device1.getRef(), "device1");
                actorToDeviceId.put(device2.getRef(), "device2");

                ActorRef queryActor = system.actorOf(DeviceGroupQuery.props(
                        actorToDeviceId,
                        1L,
                        requester.getRef(),
                        new FiniteDuration(3, TimeUnit.SECONDS)));

                assertEquals(0L, device1.expectMsgClass(Device.ReadTemperature.class).getRequestId());
                assertEquals(0L, device2.expectMsgClass(Device.ReadTemperature.class).getRequestId());

                queryActor.tell(new Device.RespondTemperature(0L, Optional.of(1.0)), device1.getRef());
                queryActor.tell(new Device.RespondTemperature(0L, Optional.of(2.0)), device2.getRef());

                DeviceGroup.RespondAllTemperatures response = requester.expectMsgClass(DeviceGroup.RespondAllTemperatures.class);
                assertEquals(1L, response.getRequestId());

                Map<String, DeviceGroup.TemperatureReading> expectedTemperatures = new HashMap<>();
                expectedTemperatures.put("device1", new DeviceGroup.Temperature(1.0));
                expectedTemperatures.put("device2", new DeviceGroup.Temperature(2.0));

                assertEquals(expectedTemperatures, response.getTemperatures());
        }

        @Test
        public void testReturnTemperatureNotAvailableForDevicesWithNoReadings() {
                TestKit requester = new TestKit(system);

                TestKit device1 = new TestKit(system);
                TestKit device2 = new TestKit(system);

                Map<ActorRef, String> actorToDeviceId = new HashMap<>();
                actorToDeviceId.put(device1.getRef(), "device1");
                actorToDeviceId.put(device2.getRef(), "device2");

                ActorRef queryActor = system.actorOf(DeviceGroupQuery.props(
                        actorToDeviceId,
                        1L,
                        requester.getRef(),
                        new FiniteDuration(3, TimeUnit.SECONDS)));

                assertEquals(0L, device1.expectMsgClass(Device.ReadTemperature.class).getRequestId());
                assertEquals(0L, device2.expectMsgClass(Device.ReadTemperature.class).getRequestId());

                queryActor.tell(new Device.RespondTemperature(0L, Optional.empty()), device1.getRef());
                queryActor.tell(new Device.RespondTemperature(0L, Optional.of(2.0)), device2.getRef());

                DeviceGroup.RespondAllTemperatures response = requester.expectMsgClass(DeviceGroup.RespondAllTemperatures.class);
                assertEquals(1L, response.getRequestId());

                Map<String, DeviceGroup.TemperatureReading> expectedTemperatures = new HashMap<>();
                expectedTemperatures.put("device1", DeviceGroup.TemperatureNotAvailable.INSTANCE);
                expectedTemperatures.put("device2", new DeviceGroup.Temperature(2.0));

                assertEquals(expectedTemperatures, response.getTemperatures());
        }
        @Test
        public void testReturnDeviceNotAvailableIfDeviceStopsBeforeAnswering() {
                TestKit requester = new TestKit(system);

                TestKit device1 = new TestKit(system);
                TestKit device2 = new TestKit(system);

                Map<ActorRef, String> actorToDeviceId = new HashMap<>();
                actorToDeviceId.put(device1.getRef(), "device1");
                actorToDeviceId.put(device2.getRef(), "device2");

                ActorRef queryActor = system.actorOf(DeviceGroupQuery.props(
                        actorToDeviceId,
                        1L,
                        requester.getRef(),
                        new FiniteDuration(3, TimeUnit.SECONDS)));

                assertEquals(0L, device1.expectMsgClass(Device.ReadTemperature.class).getRequestId());
                assertEquals(0L, device2.expectMsgClass(Device.ReadTemperature.class).getRequestId());

                queryActor.tell(new Device.RespondTemperature(0L, Optional.of(1.0)), device1.getRef());
                device2.getRef().tell(PoisonPill.getInstance(), ActorRef.noSender());

                DeviceGroup.RespondAllTemperatures response = requester.expectMsgClass(DeviceGroup.RespondAllTemperatures.class);
                assertEquals(1L, response.getRequestId());

                Map<String, DeviceGroup.TemperatureReading> expectedTemperatures = new HashMap<>();
                expectedTemperatures.put("device1", new DeviceGroup.Temperature(1.0));
                expectedTemperatures.put("device2", DeviceGroup.DeviceNotAvailable.INSTANCE);

                assertEquals(expectedTemperatures, response.getTemperatures());
        }
        @Test
        public void testReturnTemperatureReadingEvenIfDeviceStopsAfterAnswering() {
                TestKit requester = new TestKit(system);

                TestKit device1 = new TestKit(system);
                TestKit device2 = new TestKit(system);

                Map<ActorRef, String> actorToDeviceId = new HashMap<>();
                actorToDeviceId.put(device1.getRef(), "device1");
                actorToDeviceId.put(device2.getRef(), "device2");

                ActorRef queryActor = system.actorOf(DeviceGroupQuery.props(
                        actorToDeviceId,
                        1L,
                        requester.getRef(),
                        new FiniteDuration(3, TimeUnit.SECONDS)));

                assertEquals(0L, device1.expectMsgClass(Device.ReadTemperature.class).getRequestId());
                assertEquals(0L, device2.expectMsgClass(Device.ReadTemperature.class).getRequestId());

                queryActor.tell(new Device.RespondTemperature(0L, Optional.of(1.0)), device1.getRef());
                queryActor.tell(new Device.RespondTemperature(0L, Optional.of(2.0)), device2.getRef());
                device2.getRef().tell(PoisonPill.getInstance(), ActorRef.noSender());

                DeviceGroup.RespondAllTemperatures response = requester.expectMsgClass(DeviceGroup.RespondAllTemperatures.class);
                assertEquals(1L, response.getRequestId());

                Map<String, DeviceGroup.TemperatureReading> expectedTemperatures = new HashMap<>();
                expectedTemperatures.put("device1", new DeviceGroup.Temperature(1.0));
                expectedTemperatures.put("device2", new DeviceGroup.Temperature(2.0));

                assertEquals(expectedTemperatures, response.getTemperatures());
        }
        @Test
        public void testReturnDeviceTimedOutIfDeviceDoesNotAnswerInTime() {
                TestKit requester = new TestKit(system);

                TestKit device1 = new TestKit(system);
                TestKit device2 = new TestKit(system);

                Map<ActorRef, String> actorToDeviceId = new HashMap<>();
                actorToDeviceId.put(device1.getRef(), "device1");
                actorToDeviceId.put(device2.getRef(), "device2");

                ActorRef queryActor = system.actorOf(DeviceGroupQuery.props(
                        actorToDeviceId,
                        1L,
                        requester.getRef(),
                        new FiniteDuration(1, TimeUnit.SECONDS)));

                assertEquals(0L, device1.expectMsgClass(Device.ReadTemperature.class).getRequestId());
                assertEquals(0L, device2.expectMsgClass(Device.ReadTemperature.class).getRequestId());

                queryActor.tell(new Device.RespondTemperature(0L, Optional.of(1.0)), device1.getRef());

                DeviceGroup.RespondAllTemperatures response = requester.expectMsgClass(
                        java.time.Duration.ofSeconds(5),
                        DeviceGroup.RespondAllTemperatures.class);
                assertEquals(1L, response.getRequestId());

                Map<String, DeviceGroup.TemperatureReading> expectedTemperatures = new HashMap<>();
                expectedTemperatures.put("device1", new DeviceGroup.Temperature(1.0));
                expectedTemperatures.put("device2", DeviceGroup.DeviceTimedOut.INSTANCE);

                assertEquals(expectedTemperatures, response.getTemperatures());
        }
}
