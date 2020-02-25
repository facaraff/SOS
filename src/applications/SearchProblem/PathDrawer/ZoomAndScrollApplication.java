package applications.SearchProblem.PathDrawer;

import applications.SearchProblem.Particle;
import applications.SearchProblem.SimpleAgentSearchProblem;
import algorithms.inProgress.JADEDEWExponentialFreezing;
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

        SimpleAgentSearchProblem problem = new SimpleAgentSearchProblem(50, new double[] {-1.05,1.05},500,500.0,500.0,1);///SimpleAgentSearchProblem.CreateSearchProblem2(70, 500, 500, 100, 1000);


        Algorithm a = new JADEDEWExponentialFreezing();
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

        try {
            a.execute(problem, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        double[] route = a.getFinalBest();

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
