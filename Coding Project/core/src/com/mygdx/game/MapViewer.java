package com.mygdx.game;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MapViewer {

    public static void main() {
        // Create a JXMapViewer
        JXMapViewer mapViewer = new JXMapViewer();

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);

        // Set the focus
        GeoPosition chicago = new GeoPosition(41.8686, -87.6484);

        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(chicago);

        // Add interactions
        PanMouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        // Display the viewer in a JFrame
        JFrame frame = new JFrame("JXMapviewer2 Example");
        frame.getContentPane().add(mapViewer);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // new
        // Create waypoints from the geo-positions
        Set<DefaultWaypoint> waypoints = new HashSet<>();
        DefaultWaypoint initialWaypoint = new DefaultWaypoint(chicago);
        waypoints.add(initialWaypoint);

        WaypointPainter<DefaultWaypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(waypointPainter);

        // Add a mouse listener to handle clicks
        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Check if left mouse button was clicked
                    // Convert to a GeoPosition
                    GeoPosition clickPosition = mapViewer.convertPointToGeoPosition(e.getPoint());

                    // Update waypoints
                    waypoints.clear();
                    waypoints.add(new DefaultWaypoint(clickPosition));
                    waypointPainter.setWaypoints(waypoints);

                    // Refresh the map
                    mapViewer.repaint();
                }
            }
        });
    }
}
