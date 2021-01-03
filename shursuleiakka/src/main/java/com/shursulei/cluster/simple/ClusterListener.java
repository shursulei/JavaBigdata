package com.shursulei.cluster.simple;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.cluster.ClusterEvent;
import akka.cluster.typed.Cluster;
import akka.cluster.typed.Subscribe;

/**
 * 集群监听器
 */
public final class ClusterListener extends AbstractBehavior<ClusterListener.Event> {
    /**
     * 事件接口
     */
  interface Event {}

  // internal adapted cluster events only

    /***
     * 内部集群事件调整
     */
  private static final class ReachabilityChange implements Event {
    final ClusterEvent.ReachabilityEvent reachabilityEvent;
    ReachabilityChange(ClusterEvent.ReachabilityEvent reachabilityEvent) {
      this.reachabilityEvent = reachabilityEvent;
    }
  }

    /**
     * 成员修改
     */
  private static final class MemberChange implements Event {
    final ClusterEvent.MemberEvent memberEvent;
    MemberChange(ClusterEvent.MemberEvent memberEvent) {
      this.memberEvent = memberEvent;
    }
  }

  public static Behavior<Event> create() {
    return Behaviors.setup(ClusterListener::new);
  }

  private ClusterListener(ActorContext<Event> context) {
    super(context);
      /**
       * 获取集群
       */
    Cluster cluster = Cluster.get(context.getSystem());
      /**
       * 成员事件适应器
       */
    ActorRef<ClusterEvent.MemberEvent> memberEventAdapter =
        context.messageAdapter(ClusterEvent.MemberEvent.class, MemberChange::new);
      /**
       * 集群订阅
       */
    cluster.subscriptions().tell(Subscribe.create(memberEventAdapter, ClusterEvent.MemberEvent.class));

    ActorRef<ClusterEvent.ReachabilityEvent> reachabilityAdapter =
        context.messageAdapter(ClusterEvent.ReachabilityEvent.class, ReachabilityChange::new);
    cluster.subscriptions().tell(Subscribe.create(reachabilityAdapter, ClusterEvent.ReachabilityEvent.class));
  }


  @Override
  public Receive<Event> createReceive() {
    return newReceiveBuilder()
        .onMessage(ReachabilityChange.class, this::onReachabilityChange)
        .onMessage(MemberChange.class, this::onMemberChange)
        .build();
  }


  private Behavior<Event> onReachabilityChange(ReachabilityChange event) {
    if (event.reachabilityEvent instanceof ClusterEvent.UnreachableMember) {
      getContext().getLog().info("Member detected as unreachable: {}", event.reachabilityEvent.member());
    } else if (event.reachabilityEvent instanceof ClusterEvent.ReachableMember) {
      getContext().getLog().info("Member back to reachable: {}", event.reachabilityEvent.member());
    }
    return this;
  }

    /**
     * 行为成员修改
     * @param event
     * @return
     */
  private Behavior<Event> onMemberChange(MemberChange event) {
    if (event.memberEvent instanceof ClusterEvent.MemberUp) {
      getContext().getLog().info("Member is up: {}", event.memberEvent.member());
    } else if (event.memberEvent instanceof ClusterEvent.MemberRemoved) {
      getContext().getLog().info("Member is removed: {} after {}",
          event.memberEvent.member(),
          ((ClusterEvent.MemberRemoved) event.memberEvent).previousStatus()
      );
    }
    return this;
  }

}
