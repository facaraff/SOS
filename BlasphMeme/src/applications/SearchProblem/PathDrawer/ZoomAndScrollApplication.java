package applications.SearchProblem.PathDrawer;

import SearchProblem.Particle;
import SearchProblem.SimpleAgentSearchProblem;
import algorithms.JADEDEWExponentialFreezing;
import algorithms.SimulatedAnnealing;
import interfaces.Algorithm;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.List;

/**
 * An application with a zoomable and pannable canvas.
 */
public class ZoomAndScrollApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    PannableCanvas canvas;
    double drawingOffsetX = 500;
    double drawingOffsetY = 500;

    @Override
    public void start(Stage stage) {

        SimpleAgentSearchProblem problem = SimpleAgentSearchProblem.CreateSearchProblem2(70, 500, 500, 100, 1000);
        Algorithm a = new SimulatedAnnealing();
        a.setParameter("initTemp", 1.0e9);
        a.setParameter("minTemp", 0.00);
        a.setParameter("expCoolingFactor", 0.8);
        a.setParameter("perturbationFactor", 0.4);

       // a = new CMAES();
//        a = new ISPO();
//        a.setParameter("p0", 1.0);
//        a.setParameter("p1", 10.0);
//        a.setParameter("p2", 2.0);
//        a.setParameter("p3", 4.0);
//        a.setParameter("p4", 1e-5);
//        a.setParameter("p5", 30.0);

//        a = new ClassicDifferentialEvolution();
//        a.setParameter("PopulationSize", 500.0);
//        a.setParameter("F", 0.75);
//        a.setParameter("CR", 0.9);
//
        a = new JADEDEWExponentialFreezing();
        //a = new JADE();
        a.setParameter("PopulationSize", 500.0);
        a.setParameter("p", 0.05);
        a.setParameter("c", 0.05);
        a.setParameter("ArchiveSize", 2000.0);
        a.setParameter("freeze", 2.0);
        //a = new CMAES();

//        try {
//            a.execute(problem, 200000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Group group = new Group();

        Label probabilityLabel = new Label("none");
        probabilityLabel.setTextFill(Color.YELLOW);
        // create canvas
        canvas = new PannableCanvas();

        // we don't want the canvas on the top/left in this example => just
        // translate it a bit
        canvas.setTranslateX(-drawingOffsetX);
        canvas.setTranslateY(-drawingOffsetY);

        //Line line = new Line(50,49,92,400);
        //line.setStrokeWidth(3);
        //line.setStroke(Color.YELLOWGREEN);
        //Rectangle rect = new Rectangle(drawingOffsetX,drawingOffsetY,1000,1000);
        //rect.setStroke(Color.CYAN);
        //rect.setFill(Color.CYAN);
        //rect.setStrokeWidth(5);
        //canvas.getChildren().add(rect);

        double[] route = new double[]{-1.047,0.06446,0.2039,1.031,1.031,1.031,-1.047,0.01618,0.01613,0.01609,0.01604,0.01599,-1.047,-1.047,-1.047,-0.7674,0.0,0.0,0.0,-4.441E-16,4.441E-16,-4.441E-16,0.0,4.441E-16,1.047,1.047,1.047,1.047,1.047,-1.047,-1.047,-1.047,-1.047,-1.047,0.1799,0.1899,0.4022,1.047,1.047,1.047,0.8214,0.0,0.0,-0.2727,-0.2834,-0.7113,-1.047,-1.047,-1.008,0.0,8.882E-16,0.3611,1.047,1.047,1.047,1.047,0.4437,-1.036,-0.4432,-0.4554,-1.047,-1.047,-1.047,-0.136,-4.441E-16,0.5302,0.543,1.047,1.047,1.047,0.5516,0.0,-0.6222,-0.6358,-0.6495,-0.6633,-0.6772,-1.047,-0.7918,0.0,1.332E-15,0.7484,1.047,1.047,1.047,1.047,0.1111,-1.047,-1.047,-1.047,-1.047,0.8999,1.047,1.047,-1.045,-0.9641,1.047,1.007,-1.047,-1.047};
        drawParticles(problem.getParticles());
        drawRoute(problem.anglesToWaypoints(route));
        try {
            probabilityLabel.setText("POD="+(1-problem.f(route)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //canvas.getChildren().addAll(label1, label2, label3, circle1, rect1, line);

        group.getChildren().add(canvas);
        group.getChildren().add(probabilityLabel);

        // create scene which can be dragged and zoomed
        Scene scene = new Scene(group, 1024, 768);

        SceneGestures sceneGestures = new SceneGestures(canvas);
        scene.addEventFilter( MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        scene.addEventFilter( MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        scene.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        stage.setScene(scene);
        stage.show();

        canvas.addGrid();

    }

    public void drawRoute(double[] waypoints)
    {

        for (int i=0; i<waypoints.length-2; i+=2)
        {
            Line line = new Line(waypoints[i]+drawingOffsetX,waypoints[i+1]+drawingOffsetY,
                    waypoints[i+2]+drawingOffsetX, waypoints[i+3]+drawingOffsetY);
            line.setStrokeWidth(2);
            line.setStroke(Color.GREEN);
            canvas.getChildren().add(line);
        }
    }

    public void drawParticles(List<Particle> particleList)
    {
        for (Particle p : particleList)
        {
            Circle circle = new Circle(p.getX()+drawingOffsetY,p.getY()+drawingOffsetY,3,Color.RED);
            canvas.getChildren().add(circle);
        }
    }
}
