package wat.semestr7.bachelor.mvc.view.profitable;

import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

class NewDataIndicator extends Circle
{
    private Stop stop1 = new Stop(0, Color.valueOf("#2a1382"));
    private Stop stop2 = new Stop(1.0,Color.WHITE);
    private Stop stop3 = new Stop(0.5,Color.valueOf("#68cfd9"));
    private List<Stop> stops = new LinkedList<>();
    private Color stop4Color = Color.valueOf("#69d0db");
    private List<Stop> changeStop = new LinkedList<>();
    private double focusAngle=360, focusDistance=0., centerX=0.5, centerY=0.5, radius=0.6;
    private boolean proportional = true;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final Semaphore semaphore = new Semaphore(1);

    NewDataIndicator()
    {
        super();
        init();
    }

    void newData()
    {
        if(semaphore.tryAcquire())
        {
            executorService.execute(getTask());
        }
    }

    private Task<Void> getTask()
    {
        return new Task<Void>()
        {
            @Override
            protected Void call() throws Exception {
                try{
                    double var = 0.01;
                    while (var < 0.8) {
                        try { Thread.sleep(4); }
                        catch (InterruptedException e) { e.printStackTrace(); }
                        var += 0.01;
                        changeStop.remove(3);
                        changeStop.add(new Stop(var, stop4Color));
                        setFill(new RadialGradient(focusAngle, focusDistance, centerX, centerY, radius, proportional, CycleMethod.NO_CYCLE, changeStop));
                    }
                    while (var > 0.01) {
                        try { Thread.sleep(4); }
                        catch (InterruptedException e) { e.printStackTrace(); }
                        var -= 0.01;
                        changeStop.remove(3);
                        changeStop.add(new Stop(var, stop4Color));
                        setFill(new RadialGradient(focusAngle, focusDistance, centerX, centerY, radius, proportional, CycleMethod.NO_CYCLE, changeStop));
                    }
                }
                catch(Exception e){
                    System.out.println("NewDataIndicator Task broke.\n" + e);
                }
                finally{
                    semaphore.release();
                }
                return null;
            }
        };
    }

    private void init() {
        stops.add(stop1);
        stops.add(stop2);
        stops.add(stop3);
        Stop s4 = new Stop(0.0, stop4Color);
        changeStop.addAll(stops);
        changeStop.add(s4);
        RadialGradient gradient = new RadialGradient(focusAngle, focusDistance, centerX, centerY, radius, proportional, CycleMethod.NO_CYCLE, stops);
        setFill(gradient);
        setRadius(20.);
    }
}
