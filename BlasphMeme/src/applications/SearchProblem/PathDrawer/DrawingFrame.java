package applications.SearchProblem.PathDrawer;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Created by Mikolaj on 02/05/2018.
 */
public class DrawingFrame extends Application {

    private Group root;
    private Scene theScene;
//    private Stage theStage;
    private Canvas canvas;
    double xTrans,yTrans;

    @Override
    public void start(Stage primaryStage) throws Exception {

//        theStage = primaryStage;

        primaryStage.setTitle("Route Viewer");

        root = new Group();theScene = new Scene( root );
        primaryStage.setScene(theScene);

        canvas = new Canvas( 400, 400 );
        root.getChildren().add(canvas);

        theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) 
            {
                switch (event.getCode()) {
                    case UP:
                        yTrans = -50; xTrans=0;
                        break;
                    case DOWN:
                        yTrans = 50; xTrans=0;
                        break;
                    case LEFT:
                        xTrans = -50; yTrans=0;
                        break;
                    case RIGHT:
                        xTrans = 50; yTrans=0;
                        break;
				default:
					break;
                }
                redraw();
            }
        });


//        Button leftButton = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                canvas.setTranslateX(xTrans);
//                canvas.setTranslateY(yTrans);
//                xTrans+=50;
//                yTrans+=50;
//                redraw();
//            }
//        });
//
//        root.getChildren().add(btn);

        redraw();
        primaryStage.show();
    }

    public void redraw()
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());

        gc.translate(xTrans,yTrans);

        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        Font theFont = Font.font("Times New Roman", FontWeight.BOLD, 48);
        gc.setFont(theFont);
        gc.fillText("Hello, World!", 60, 50);
        //gc.strokeText( "Hello, World!", 60, 50 );

        gc.strokeLine(-50.0, 0.0, 50.0, 50.0);
        gc.strokeLine(50.0, 50.0, 150.0, 300.0);
        gc.strokeLine(150.0, 300.0, 500.0, 500.0);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
