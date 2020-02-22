//package edu.etsu.glosa.glosa.backend.transform;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
/*
import Exceptions.TrafficSignaledIntersectionException;
import Facade.ITrafficSignaledIntersectionService;
import SAEJ7235Integrations.MAPStructure.Connection;
import SAEJ7235Integrations.MAPStructure.GenericLane;
import SAEJ7235Integrations.MAPStructure.IntersectionGeometry;
import SAEJ7235Integrations.MAPStructure.MapData;
import SAEJ7235Integrations.SAEJ7235Message;
import SAEJ7235Integrations.SPaTStructure.IntersectionState;
import SAEJ7235Integrations.SPaTStructure.MovementEvent;
import SAEJ7235Integrations.SPaTStructure.MovementState;
import SAEJ7235Integrations.SPaTStructure.SPAT;
import SAEJ7235Integrations.SPaTStructure.SignalGroupID;
import edu.etsu.glosa.glosa.backend.models.ActiveStatus;
import edu.etsu.glosa.glosa.backend.models.Color;
import edu.etsu.glosa.glosa.backend.models.Direction;
import edu.etsu.glosa.glosa.backend.models.Intersection;
import edu.etsu.glosa.glosa.backend.models.Lane;
import edu.etsu.glosa.glosa.backend.models.LaneDirection;
import edu.etsu.glosa.glosa.backend.models.TrafficLight;*/

public class TransformBackE implements iTransformBackE {
    /*private Context mContext;
    private ITrafficSignaledIntersectionService mTrafficSignaledIntersectionService;
    private SPAT spat;
    private MapData mapData;
    private SignalGroupID signalGroupID;
    List<Intersection> intersections;

    public Transform(Context context, ITrafficSignaledIntersectionService trafficSignaledIntersectionService) {
        mContext = context;
        mTrafficSignaledIntersectionService = trafficSignaledIntersectionService;*/
}

//@Override
    /*public Intersection getSpatData(double latitude,double longitude, double orientation) throws Exception {
        try {
            SAEJ7235Message response = mTrafficSignaledIntersectionService.getSAEJ7235Message(
                    latitude,
                    longitude,
                    orientation
            );
            intersections = new ArrayList<Intersection>();

            for (int i = 0; i < response.get_spat().size(); i++) {
                this.spat = response.get_spat().get(i);
                this.mapData = response.get_map().get(i);
                intersections.add(transformData());
            }
        } catch (TrafficSignaledIntersectionException exception)
        {
            throw exception;
        } catch (Exception exception)
        {
            throw new Exception("Unable to retrieve response.", exception);
        }

        intersections.get(0).refPoint = mapData.intersections.elements[0].refPoint;

        return intersections.get(0);
    }*/

    /*private Intersection transformData() {
        Intersection intersection = new Intersection();
        IntersectionState spat_intersections = spat.intersections.elements[0];
        intersection.name = spat_intersections.name.getValue();
        for (int i = 0; i < spat_intersections.states.getLength(); i++) {
            signalGroupID = spat_intersections.states.elements[i].signalGroup;
            Lane lane = new Lane();
            lane.laneId = (int) signalGroupID.getValue();
            lane.laneDirections = getLaneDirections(spat_intersections.states.elements[i]);
            intersection.lanes.add(lane);
        }
        return intersection;
    }*/

    /*private List<LaneDirection> getLaneDirections(MovementState movmentState) {
        List<LaneDirection> laneDirections = new ArrayList<LaneDirection>();
        MovementEvent movementEvent = movmentState.state_time_speed.elements[0];
        TrafficLight trafficLight = new TrafficLight();
        switch (movementEvent.eventState.getValue()) {
            case 6:
                trafficLight.color = Color.GREEN;
                break;
            case 3:
                trafficLight.color = Color.RED;
                break;
            case 8:
                trafficLight.color = Color.YELLOW;

        }
        //using movmentState.state_time_speed.elements[1] because the timing of current light is alway 0 while the time of the next prediction
        // is in the next array of the element
        trafficLight.duration = (int) (movmentState.state_time_speed.elements[1].timing.minEndTime.getValue());
        trafficLight.activeStatus = ActiveStatus.ACTIVE;

        for (String direction : get_direction()) {
            LaneDirection laneDirection = new LaneDirection();
            laneDirection.phase = trafficLight;
            laneDirection.direction = Direction.valueOf(direction.toUpperCase());
            laneDirections.add(laneDirection);
        }


        return laneDirections;
    }

    private List<String> get_direction() {
        List<String> rtn = new ArrayList<String>();
        for (IntersectionGeometry intersection : mapData.intersections.elements) {
            for (GenericLane lane : intersection.laneSet.elements) {
                for (Connection conn : lane.connectsTo.elements) {
                    if (conn.signalGroup.getValue() == signalGroupID.getValue()) {
                        rtn.add(conn.connectingLane.maneuver.getValue());
                    }
                }
            }
        }
        return rtn;
    }*/
}