package com.shursulei.quickstart;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO * @version 1.0 * @author shursulei * @date 2020/12/2 19:33
 * 聊天室
 */
public class ChatRoom {
    /**
     * 聊天室接口
     */
    static interface RoomCommand {}

    /**
     *获取会话
     * :screenName:会话名
     * :SessionEvent:会话事件
     */
    public static final class GetSession implements RoomCommand {
        public final String screenName;
        public final ActorRef<SessionEvent> replyTo;

        public GetSession(String screenName, ActorRef<SessionEvent> replyTo) {
            this.screenName = screenName;
            this.replyTo = replyTo;
        }
    }

    /**
     * 会话事件
     */
    interface SessionEvent {}

    /**
     * 会话授权
     */
    public static final class SessionGranted implements SessionEvent {
        public final ActorRef<PostMessage> handle;

        public SessionGranted(ActorRef<PostMessage> handle) {
            this.handle = handle;
        }
    }

    /**
     * 会话拒绝
     */
    public static final class SessionDenied implements SessionEvent {
        public final String reason;

        public SessionDenied(String reason) {
            this.reason = reason;
        }
    }

    /**
     * 消息发送
     */
    public static final class MessagePosted implements SessionEvent {
        public final String screenName;/*会话名*/
        public final String message;/*消息*/

        public MessagePosted(String screenName, String message) {
            this.screenName = screenName;
            this.message = message;
        }
    }

    /**
     * 会话接口
     */
    interface SessionCommand {}

    /**
     * 发送消息
     */
    public static final class PostMessage implements SessionCommand {
        public final String message;

        public PostMessage(String message) {
            this.message = message;
        }
    }

    /**
     * 通知客户端类
     */
    private static final class NotifyClient implements SessionCommand {
        final MessagePosted message;

        NotifyClient(MessagePosted message) {
            this.message = message;
        }
    }

    /**
     * 发布会话消息
     */
    private static final class PublishSessionMessage implements RoomCommand {
        public final String screenName;
        public final String message;

        public PublishSessionMessage(String screenName, String message) {
            this.screenName = screenName;
            this.message = message;
        }
    }

    public static Behavior<RoomCommand> create() {
        return Behaviors.setup(
                ctx -> new ChatRoom(ctx).chatRoom(new ArrayList<ActorRef<SessionCommand>>()));
    }

    private final ActorContext<RoomCommand> context;

    private ChatRoom(ActorContext<RoomCommand> context) {
        this.context = context;
    }

    private Behavior<RoomCommand> chatRoom(List<ActorRef<SessionCommand>> sessions) {
        return Behaviors.receive(RoomCommand.class)
                .onMessage(GetSession.class, getSession -> onGetSession(sessions, getSession))
                .onMessage(PublishSessionMessage.class, pub -> onPublishSessionMessage(sessions, pub))
                .build();
    }

    private Behavior<RoomCommand> onGetSession(
            List<ActorRef<SessionCommand>> sessions, GetSession getSession)
            throws UnsupportedEncodingException {
        ActorRef<SessionEvent> client = getSession.replyTo;
        ActorRef<SessionCommand> ses =
                context.spawn(
                        Session.create(context.getSelf(), getSession.screenName, client),
                        URLEncoder.encode(getSession.screenName, StandardCharsets.UTF_8.name()));
        // narrow to only expose PostMessage
        client.tell(new SessionGranted(ses.narrow()));
        List<ActorRef<SessionCommand>> newSessions = new ArrayList<>(sessions);
        newSessions.add(ses);
        return chatRoom(newSessions);
    }

    /**
     * 发布会话消息
     * @param sessions
     * @param pub
     * @return
     */
    private Behavior<RoomCommand> onPublishSessionMessage(
            List<ActorRef<SessionCommand>> sessions, PublishSessionMessage pub) {
        NotifyClient notification =
                new NotifyClient((new MessagePosted(pub.screenName, pub.message)));
        sessions.forEach(s -> s.tell(notification));
        return Behaviors.same();
    }

    /**
     * 静态会话类
     */
    static class Session {
        static Behavior<ChatRoom.SessionCommand> create(
                ActorRef<RoomCommand> room, String screenName, ActorRef<SessionEvent> client) {
            return Behaviors.receive(ChatRoom.SessionCommand.class)
                    .onMessage(PostMessage.class, post -> onPostMessage(room, screenName, post))
                    .onMessage(NotifyClient.class, notification -> onNotifyClient(client, notification))
                    .build();
        }

        private static Behavior<SessionCommand> onPostMessage(
                ActorRef<RoomCommand> room, String screenName, PostMessage post) {
            // from client, publish to others via the room
            room.tell(new PublishSessionMessage(screenName, post.message));
            return Behaviors.same();
        }

        private static Behavior<SessionCommand> onNotifyClient(
                ActorRef<SessionEvent> client, NotifyClient notification) {
            // published from the room
            client.tell(notification.message);
            return Behaviors.same();
        }
    }
}
