package com.example.springattempt;

import lombok.extern.java.Log;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineSystemConstants;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

import java.sql.Time;       // added to play with timers
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
public class SpringattemptApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SpringattemptApplication.class, args);    // running the machine
        
        System.out.println("Hello World");  // displaying output to test machine
    }
    
}

/**
 * The following ENUM values are subject to change.
 */
enum Events {
    ACTIVATE_DEVICE,                      // for UI and SPaT
    ACTIVATE_UI_AND_SPAT,
    CANCELLED_REQUEST,
    INITIATE_SHUTDOWN,
    SHUTDOWN_CONFIRMED,
    ACTIVATE_UI_STANDBY,                  // UI Events
    SPAT_READY_FOR_UI_DISPLAY,
    DATA_RECEIVED_FROM_SPAT,
    SPAT_SIGNALED_INTERSECTION_COMPLETE,
    ACTIVATE_SPAT_STANDBY,                // SPaT Events
    IN_RANGE,
    COLLECT_DATA,
    CALCULATE_MATH,
    ACTIVATE_UI_DISPLAY,
    WHILE_SAFE,
    NOT_SAFE,
    THROUGH_INTERSECTION,
    OUT_OF_RANGE,
    SIGNAL_UI_STANDBY
}

/**
 * The following ENUM values are subject to change.
 */
enum States{
    TIMER_ACTIVATED,
    TIMER_ENDED,
    INITIAL_STATE,          // for UI and SPaT
    DEVICE_ACTIVATED,
    DEVICE_DEACTIVATED,
    UI_SPAT_PARENT,
    SHUTDOWN_INITIATED,
    UI_ACTIVATED,            // UI States
    UI_STANDBY,
    UI_DISPLAY_WAITING,
    ADVISORY_DISPLAYED,
    SPAT_ACTIVATED,
    UI_DEACTIVATED,
    SPAT_STANDBY,            // SPaT States
    TRIGGER_ADV_DISPLAY,
    TRIGGER_ADV_CYCLE,
    INTERSECTION_IDENTIFIED,
    INTERSECTION_COMPLETE,
    DATA_COLLECTED,
    CALCULATIONS_COMPLETE,
    UI_DISPLAY_READY,
    ADVISORY_READY,
    DISPLAY_SPEED_RANGE,
    DISPLAY_STOP,
    SPAT_PREP_FOR_STANDBY,
    SPAT_DEACTIVATED
}

/**
 * Application Runner
 */
@Log
@Component
class Runner implements ApplicationRunner{
    
    private final StateMachineFactory<States, Events> factory;
    
    Runner(StateMachineFactory<States, Events> factory)   // ignore this error - IDE flagging - still runs
    {
        this.factory = factory;
        
    }
    
    
    /**
     * Create and run a state machine
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception
    {
        // creates a machine
        StateMachine<States, Events> machine = this.factory.getStateMachine("13232");   // this is made up and not a real machine id
        machine.start();   // starts the machine
        
        // manually activating events to trigger states in the machine
        log.info("current state: " + machine.getState().getId().name());
        machine.sendEvent(Events.ACTIVATE_DEVICE);
        log.info("current state: " + machine.getState().getId().name());
        
       
        System.out.println("Waiting 30 seconds...");
        Thread.sleep(30000);                            // time to run the program
        
        machine.sendEvent(Events.CANCELLED_REQUEST);        // emulating being cancelled by the user and stopping the timer
        //Thread.sleep(10000);
        //machine.stop();                                   // manually stopping the machine  **TO DO: fix and have stop in state Timer Ended
        //System.out.println("MACHINE STOPPED");
        
    
       
    }
}

@Log
@Configuration
@EnableStateMachineFactory
class SimpleEnumStatemachineConfiguration extends StateMachineConfigurerAdapter<States, Events>
{
    long start = System.currentTimeMillis();
    
    /**
     * Setup the transitions and the states events involved
     *
     * @param transitions
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception
    {
        // assigns events to initial and targeted states, and their order
        transitions
                
                // main thread
                .withExternal()
                .source(States.INITIAL_STATE).target(States.DEVICE_ACTIVATED).event(Events.ACTIVATE_DEVICE)
                .and()
                
                // forking to 3 threads
                .withFork().source(States.DEVICE_ACTIVATED).target(States.UI_SPAT_PARENT)
                .and()
                
                // timer thread
                .withExternal().source(States.TIMER_ACTIVATED).target(States.TIMER_ENDED).event(Events.CANCELLED_REQUEST)
                .and()
                
                // UI thread
                .withExternal().source(States.UI_ACTIVATED).target(States.UI_STANDBY).event(Events.ACTIVATE_UI_STANDBY)
                .and()
                    .withExternal().source(States.UI_STANDBY).target(States.UI_DISPLAY_WAITING).event(Events.SPAT_READY_FOR_UI_DISPLAY)
                    .and()
                
                // SPaT thread
                .withExternal().source(States.SPAT_ACTIVATED).target(States.SPAT_STANDBY)//.event(Events.ACTIVATE_SPAT_STANDBY)
                .and()
                
                // joining all threads
                .withJoin().source(States.SPAT_STANDBY).source(States.UI_DISPLAY_WAITING).source(States.TIMER_ENDED).target(States.SHUTDOWN_INITIATED)
                .and()
                .withExternal().source(States.SHUTDOWN_INITIATED).target(States.DEVICE_DEACTIVATED).event(Events.SHUTDOWN_CONFIRMED);
        
    }
    
    /**
     * Configure the states involved in the machine
     *
     * @param states
     * @throws Exception
     */
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception
    {
        states
                // main thread
                .withStates()
                .join(States.SHUTDOWN_INITIATED)
                .initial(States.INITIAL_STATE)
                    .stateEntry(States.INITIAL_STATE,initialAction())
                .fork(States.DEVICE_ACTIVATED)
                    .stateEntry(States.DEVICE_ACTIVATED, activdAction())
                .state(States.UI_SPAT_PARENT, parentAction())
                .state(States.ADVISORY_DISPLAYED)
                .state(States.UI_DEACTIVATED)
                .state(States.TRIGGER_ADV_CYCLE)
                .end(States.DEVICE_DEACTIVATED)
                .stateEntry(States.DEVICE_DEACTIVATED, deactAction()) // final state
                .and()
                
                // timer thread
                .withStates()
                    .parent(States.UI_SPAT_PARENT)
                    .initial(States.TIMER_ACTIVATED)
                        .stateEntry(States.TIMER_ACTIVATED, timerActAction())
                    .end(States.TIMER_ENDED)
                        .stateEntry(States.TIMER_ENDED, timerEndAction())
                    .and()
                
                // UI thread
                .withStates()
                    .parent(States.UI_SPAT_PARENT)
                    .initial(States.UI_ACTIVATED)
                        .stateEntry(States.UI_ACTIVATED,uiActvAction())
                    .state(States.UI_STANDBY, uiStandbyAction())
                    .end(States.UI_DISPLAY_WAITING)
                        .stateEntry(States.UI_DISPLAY_WAITING, uiDisplWAction())
                    .and()
                
                // SPaT Thread
                .withStates()
                    .parent(States.UI_SPAT_PARENT)
                    .initial(States.SPAT_ACTIVATED, spatActvAction())
                    .end(States.SPAT_STANDBY)
                        .stateEntry(States.SPAT_STANDBY,spatStdbyAction());
                
                
        
        
    }
    
    /**
     * The "engine" behind the machine
     *
     * @param config
     * @throws Exception
     */
    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception
    {
        StateMachineListenerAdapter<States, Events> adapter = new StateMachineListenerAdapter<States, Events>()
        {
            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to)
            {
                log.info(String.format("stateChanged(from: %s, to: %s)", from + "", to + ""));
            }
        };
        config.withConfiguration()
                .taskExecutor(taskExecutor())
                .autoStartup(true).listener(adapter);
    }
    
    /**
     * Task Executor - allows multithreading
     *
     * @return
     */
    @Bean(name = StateMachineSystemConstants.TASK_EXECUTOR_BEAN_NAME)
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        return taskExecutor;
    }
    
    /*******************************************************************
     * The following are ACTIONS that are called from the above states
     *******************************************************************/
    
    /** ACTION for Timer Activated called from states **/
    @Bean
    public Action<States, Events> timerActAction() {
        return new Action<States, Events>() {
            
            @Override
            public void execute(StateContext<States, Events> context) {
    
                System.out.print("\nTimer Activated\n\n");
    
                // triggers event for Timer Ended
                //context.getStateMachine().sendEvent(Events.CANCELLED_REQUEST);
                
            }
        };
    }
    
    /** ACTION for Timer Ended called from states **/
    @Bean
    public Action<States, Events> timerEndAction() {
        return new Action<States, Events>() {
            
            @Override
            public void execute(StateContext<States, Events> context) {
                
                System.out.print("\nTimer Ended");
                long finish = System.currentTimeMillis();
                long timeElapsed = finish - start;
                System.out.print("\nElapsed Time is " + timeElapsed + "ms\n\n");
                context.getStateMachine().stop();
            }
        };
    }
    
    /** ACTION for UI Activated called from states **/
    @Bean
    public Action<States, Events> uiActvAction() {
        return new Action<States, Events>() {
            
            @Override
            public void execute(StateContext<States, Events> context) {
                try
                {
                    System.out.print("\nUI Activated Started\n\n");
                    for(int i = 100; i <= 149; i++) {
                        if(i % 3 == 0){
                            System.out.println("Waiting 1 seconds...");
                            Thread.sleep(1000);
                        }
                        System.out.print(i + " ");
                    }
                    System.out.print("\nUI Activated Done\n\n");
                    
                    // triggers event for UI Standby
                    context.getStateMachine().sendEvent(Events.ACTIVATE_UI_STANDBY);
                }
                catch(Exception e){
                    System.out.println("Oops..error");
                }
            }
        };
    }
    
    /** ACTION for UI Standby called from states **/
    @Bean
    public Action<States, Events> uiStandbyAction() {
        return new Action<States, Events>() {
            
            @Override
            public void execute(StateContext<States, Events> context) {
                try
                {
                    
                    System.out.print("\nUI Standby Started\n\n");
                    for(int i = 300; i <= 349; i++) {
                        if(i % 5 == 0){
                            System.out.println("Waiting 1 seconds...");
                            Thread.sleep(1000);
                        }
                        System.out.print(i + " ");
                    }
                    System.out.print("\nUI Standby Done\n\n");
    
                    // triggers event for UI Display Waiting
                    context.getStateMachine().sendEvent(Events.SPAT_READY_FOR_UI_DISPLAY);
                }
                catch(Exception e){
                    System.out.println("Oops..error");
                }
            }
        };
    }
    
    /** ACTION for UI Display Waiting called from states **/
    @Bean
    public Action<States, Events> uiDisplWAction() {
        return new Action<States, Events>() {
            
            @Override
            public void execute(StateContext<States, Events> context) {
                
                try
                {
                    System.out.print("\nUI Display Waiting Started\n\n");
                    for(int i = 500; i <= 549; i++) {
                        if(i % 4 == 0){
                            System.out.println("Waiting 1 seconds...");
                            Thread.sleep(1000);
                        }
                        System.out.print(i + " ");
                    }
                    System.out.print("\nUI Display Waiting Done\n\n");
                    context.getStateMachine().sendEvent(Events.CANCELLED_REQUEST);
                }
                catch(Exception e){
                    System.out.println("Oops..error");
                }
                
            }
        };
    }
    
    /** ACTION for SPaT Activated called from states **/
    @Bean
    public Action<States, Events> spatActvAction() {
        return new Action<States, Events>() {
            
            @Override
            public void execute(StateContext<States, Events> context) {
                try
                {
                    
                    System.out.print("\nSPaT Activated Started\n\n");
                    for(int i = 200; i <= 249; i++) {
                        if(i % 4 == 0){
                            System.out.println("Waiting 1 seconds...");
                            Thread.sleep(1000);
                        }
                        System.out.print(i + " ");
                        
                    }
                    System.out.print("\nSPaT Activated Done\n\n");
    
                    // triggers event for SPaT Standby
                    context.getStateMachine().sendEvent(Events.ACTIVATE_SPAT_STANDBY);
                }
                catch(Exception e){
                    System.out.println("Oops..error");
                }
                
            }
        };
    }
    
    /** ACTION for SPaT Standby called from states **/
    @Bean
    public Action<States, Events> spatStdbyAction() {
        return new Action<States, Events>() {
            
            @Override
            public void execute(StateContext<States, Events> context) {
                try
                {
                    
                    
                    System.out.print("\nSPaT Standby Started\n\n");
                    for(int i = 400; i <= 449; i++) {
                        if(i % 6 == 0){
                            System.out.println("Waiting 1 seconds...");
                            Thread.sleep(1000);
                        }
                        System.out.print(i + " ");
                    }
                    System.out.print("\nSPaT Standby Done\n\n");
                }
                catch(Exception e){
                    System.out.println("Oops..error");
                }
            }
        };
    }
    
    /** ACTION for Initial Action called from states **/
    @Bean
    public Action<States, Events> initialAction() {
        return new Action<States, Events>() {
            
            @Override
            public void execute(StateContext<States, Events> context) {
                // do something
                System.out.println("\n Initial State Started\n");
            }
        };
    }
    
    /** ACTION for UI SPaT Parent called from states **/
    @Bean
    public Action<States, Events> parentAction() {
        return new Action<States, Events>() {
            
            @Override
            public void execute(StateContext<States, Events> context) {
                // do something
                System.out.println("\n UI SPAT PARENT ACTIVATED\n\n");  // not displaying
            }
        };
    }
    
    /** ACTION for Device Activated called from states **/
    @Bean
    public Action<States, Events> activdAction() {
        return new Action<States, Events>() {
            
            @Override
            public void execute(StateContext<States, Events> context) {
                // do something
                System.out.println("\n Device ACTIVATED\n\n");  // not displaying
            }
        };
    }
    
    /** ACTION for Device Deactivated called from states **/
    @Bean
    public Action<States, Events> deactAction() {
        return new Action<States, Events>() {
            
            @Override
            public void execute(StateContext<States, Events> context) {
                // do something
                System.out.print("\nDevice Deactivated\n");
                //machine.stop();
                //context.getStateMachine().stop();
            }
        };
    }
}
