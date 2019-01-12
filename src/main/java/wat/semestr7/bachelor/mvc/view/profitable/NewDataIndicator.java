package wat.semestr7.bachelor.mvc.view.profitable;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

import java.util.LinkedList;
import java.util.List;

public class NewDataIndicator extends Circle
{
    private Stop s1 = new Stop(0, Color.valueOf("#2a1382"));
    private Stop s2 = new Stop(1.0,Color.WHITE);
    private Stop s3 = new Stop(0.5,Color.valueOf("#68cfd9"));
    private Stop s4;
    private List<Stop> stop = new LinkedList<>();
    private Color s4Color = Color.valueOf("#69d0db");
    private List<Stop> changeStop = new LinkedList<>();
    private double focusAngle =360, focusDistance = 0., centerX=0.5, centerY=0.5, radius=0.6;
    private boolean proportional = true;
    private RadialGradient gradient;

    private Boolean isIndicating = false;
    private final Object lock = new Object();

    public NewDataIndicator()
    {
        super();
        init();
    }

    private void init() {
        stop.add(s1);
        stop.add(s2);
        stop.add(s3);

        s4 = new Stop(0.0, s4Color);
        changeStop.addAll(stop);
        changeStop.add(s4);
        gradient = new RadialGradient(focusAngle, focusDistance, centerX, centerY, radius, proportional, CycleMethod.NO_CYCLE, stop);
        setFill(gradient);
        setRadius(20.);
    }
    public void newData()
    {
        if(!getIndicating())
        {
            setIndicating(true);
            new Thread(() -> {
                double var = 0.01;
                while(var < 0.8)
                {
                    try {Thread.sleep(6);}
                    catch (InterruptedException e) { e.printStackTrace(); }
                    var+=0.01;
                    changeStop.remove(3);
                    changeStop.add(new Stop(var,s4Color));
                    setFill(new RadialGradient(focusAngle,focusDistance,centerX,centerY,radius,proportional,CycleMethod.NO_CYCLE,changeStop));
                }
                while(var > 0.01)
                {
                    try {Thread.sleep(6);}
                    catch (InterruptedException e) { e.printStackTrace(); }
                    var-=0.01;
                    changeStop.remove(3);
                    changeStop.add(new Stop(var,s4Color));
                    setFill(new RadialGradient(focusAngle,focusDistance,centerX,centerY,radius,proportional,CycleMethod.NO_CYCLE,changeStop));
                }
                setIndicating(false);
            }
            ).start();
        }
    }

    private Boolean getIndicating() {
        synchronized (lock)
        {
            return isIndicating;
        }
    }

    private void setIndicating(Boolean indicating) {
        synchronized (lock)
        {
            isIndicating = indicating;
        }
    }
}
