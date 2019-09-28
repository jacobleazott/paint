package sample;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.image.WritableImage;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.Group;
import java.util.ArrayList;

import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;

public class PaintDrawActions {
    private Image img;
    private PaintMenuBar menubar;
    private final int MIN_HEIGHT = 90;
    private final int MIN_WIDTH = 90;
    private final int TOOL_BAR_H_GAP = 5;
    private final int SLIDER_MIN = 1;
    private final int SLIDER_MAX = 50;
    private final int SLIDER_INITIAL = 3;
    private final int GRAPHICS_CONTEXT_LINE_WIDTH = 1;
    private final int CANVAS_ORIGIN_X = 0;
    private final int CANVAS_ORIGIN_Y = 0;
    private WritableImage selimg;
    private Group group;
    private Canvas tmp_canvas;
    WritableImage tmp_snap;


    void setImage(Image img){
        this.img = img;
    }

    public void drawNPolygon(int n, GraphicsContext gc, double x0, double y0, double r){
        double[] xArray = new double[n], yArray = new double[n];
        for (int i = 0; i < n; i++){
            xArray[i] = r * Math.cos(2 * i * Math.PI / n) + x0;
            yArray[i] = r * Math.sin(2 * i * Math.PI / n) + y0;
        }
        gc.strokePolygon(xArray, yArray, n);
        gc.fillPolygon(xArray, yArray, n);
    }

    public ArrayList createNPolygon(int n, double x0, double y0, double r){
        ArrayList<Double> points = new ArrayList<Double>(2*n);
        for(int i = 0; i < n; i++){
            points.add(r * Math.cos(2 * i * Math.PI / n) + x0);
            points.add(r * Math.sin(2 * i * Math.PI / n) + y0);
        }
        return points;
    }

    VBox setup(Canvas canvas, GraphicsContext gc) {
        //AtomicReference<Canvas> tmp_canvas = null;

        PaintWindow window = new PaintWindow();
        PaintMenuBar menubar = new PaintMenuBar();

        Stack<Shape> undoHistory = new Stack();
        Stack<Shape> redoHistory = new Stack();
        /* ----------btns---------- */
        ToggleButton selbtn = new ToggleButton("Selection");
        ToggleButton movbtn = new ToggleButton("Move");
        ToggleButton drowbtn = new ToggleButton("Draw");
        ToggleButton rubberbtn = new ToggleButton("Erase");
        ToggleButton linebtn = new ToggleButton("Line");
        ToggleButton rectbtn = new ToggleButton("Rectangle");
        ToggleButton circlebtn = new ToggleButton("Circle");
        ToggleButton elpslebtn = new ToggleButton("Ellipse");
        ToggleButton tribtn = new ToggleButton("Triangle");
        ToggleButton polybtn = new ToggleButton("Polygon");
        ToggleButton textbtn = new ToggleButton("Text");
        ToggleButton dropperbtn = new ToggleButton("Dropper");

        ToggleButton[] toolsArr = {selbtn, movbtn, drowbtn, rubberbtn, linebtn, rectbtn, circlebtn, elpslebtn, tribtn,
                            polybtn, textbtn, dropperbtn};

        ToggleGroup tools = new ToggleGroup();

        for (ToggleButton tool : toolsArr) {
            tool.setMinWidth(MIN_HEIGHT);
            tool.setToggleGroup(tools);
            tool.setCursor(Cursor.HAND);
        }

        ColorPicker cpLine = new ColorPicker(Color.BLACK);
        ColorPicker cpFill = new ColorPicker(Color.TRANSPARENT);

        TextArea text = new TextArea();
        text.setPrefRowCount(1);
        Slider slider = new Slider(SLIDER_MIN, SLIDER_MAX, SLIDER_INITIAL);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        Label line_color = new Label("Line Color");
        Label fill_color = new Label("Fill Color");
        Label line_width = new Label("3.0");

        Button undo = new Button("Undo");
        Button redo = new Button("Redo");
        Button save = new Button("Save");
        Button open = new Button("Open");

        Button[] basicArr = {undo, redo, save, open};

        for (Button btn : basicArr) {
            btn.setMinWidth(MIN_WIDTH);
            btn.setCursor(Cursor.HAND);
            btn.setTextFill(Color.WHITE);
            btn.setStyle("-fx-background-color: #666;");
        }
        save.setStyle("-fx-background-color: #80334d;");
        open.setStyle("-fx-background-color: #80334d;");

        VBox btns = new VBox(2*TOOL_BAR_H_GAP);
        btns.getChildren().addAll(selbtn, movbtn, drowbtn, rubberbtn, linebtn, rectbtn, circlebtn, elpslebtn, tribtn, polybtn,
                textbtn, text,dropperbtn, line_color, cpLine, fill_color, cpFill, line_width, slider, undo, redo);
        btns.setPadding(new Insets(TOOL_BAR_H_GAP));
        btns.setStyle("-fx-background-color: #999");
        btns.setPrefWidth(MIN_WIDTH+2*TOOL_BAR_H_GAP);

        /* ----------Draw Canvas---------- */
        gc.setLineWidth(GRAPHICS_CONTEXT_LINE_WIDTH);

        Line line = new Line();
        Rectangle rect = new Rectangle();
        Rectangle selrect = new Rectangle();
        selrect.setCursor(Cursor.MOVE);
        Circle circ = new Circle();
        Ellipse elps = new Ellipse();
        Polygon triangle = new Polygon();
        Line polygon = new Line();


        canvas.setOnMousePressed(e -> {
            tmp_snap = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
            canvas.snapshot(null, tmp_snap);
            if (drowbtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.beginPath();
                gc.lineTo(e.getX(), e.getY());
            } else if (rubberbtn.isSelected()) {
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            } else if (linebtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                line.setStartX(e.getX());
                line.setStartY(e.getY());
                line.setEndX(e.getX());
                line.setEndY(e.getY());
                //gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
                line.setStroke(Paint.valueOf("#000000"));
                line.setStrokeWidth(gc.getLineWidth());
                //window.addToGroup(line);

            } else if (rectbtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                rect.setX(e.getX());
                rect.setY(e.getY());
            } else if (circlebtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                circ.setCenterX(e.getX());
                circ.setCenterY(e.getY());
            } else if (elpslebtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                elps.setCenterX(e.getX());
                elps.setCenterY(e.getY());
            } else if (textbtn.isSelected()) {
                gc.setLineWidth(1);
                gc.setFont(Font.font(slider.getValue()));
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                gc.fillText(text.getText(), e.getX(), e.getY());
                gc.strokeText(text.getText(), e.getX(), e.getY());
            } else if (dropperbtn.isSelected()){
                gc.beginPath();
                double x0 = e.getX();
                double y0 = e.getY();
                PixelReader colordropper = img.getPixelReader();
                Color newColor = colordropper.getColor((int)x0, (int)y0);
                //newColor = cpFill.getValue();
                cpFill.setValue(newColor);
                cpFill.getCustomColors().add(newColor);
                //cpFill.getCustomColors().set(0, newColor);
                //gc.setFill(newColor);
            } else if (selbtn.isSelected()){
                //gc.setStroke(cpLine.getValue());
                //Paint fill = Paint.valueOf("#000000");
                gc.setFill(Paint.valueOf("#FFFFFF"));
                selrect.setX(e.getX());
                selrect.setY(e.getY());
                System.out.println("WOrks");
            } else if (tribtn.isSelected()){
                triangle.getPoints().add(0, e.getX());
                triangle.getPoints().add(1, e.getY());
            } else if (polybtn.isSelected()){
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                polygon.setStartX(e.getX());
                polygon.setStartY(e.getY());
            }
        });

        canvas.setOnMouseDragged(e -> {
            if (drowbtn.isSelected()) {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
            } else if (rubberbtn.isSelected()) {
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            } else if (linebtn.isSelected()){
                gc.drawImage(tmp_snap, 0, 0);
                //window.removeToGroup(line);
                line.setEndX(e.getX());
                line.setEndY(e.getY());
                gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());

            } else if (rectbtn.isSelected()){
                gc.drawImage(tmp_snap, 0, 0);
                rect.setWidth(Math.abs((e.getX() - rect.getX())));
                rect.setHeight(Math.abs((e.getY() - rect.getY())));
                //rect.setX((rect.getX() > e.getX()) ? e.getX(): rect.getX());
                if (rect.getX() > e.getX()) {
                    rect.setX(e.getX());
                }
                //rect.setY((rect.getY() > e.getY()) ? e.getY(): rect.getY());
                if (rect.getY() > e.getY()) {
                    rect.setY(e.getY());
                }

                gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

                //undoHistory.push(new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()));
            } else if (circlebtn.isSelected()){
                gc.drawImage(tmp_snap, 0, 0);
                circ.setRadius((Math.abs(e.getX() - circ.getCenterX()) + Math.abs(e.getY() - circ.getCenterY())) / 2);

                if (circ.getCenterX() > e.getX()) {
                    circ.setCenterX(e.getX());
                }
                if (circ.getCenterY() > e.getY()) {
                    circ.setCenterY(e.getY());
                }

                gc.fillOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());
                gc.strokeOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());
            } else if (elpslebtn.isSelected()){
                gc.drawImage(tmp_snap, 0, 0);
                elps.setRadiusX(Math.abs(e.getX() - elps.getCenterX()));
                elps.setRadiusY(Math.abs(e.getY() - elps.getCenterY()));

                if (elps.getCenterX() > e.getX()) {
                    elps.setCenterX(e.getX());
                }
                if (elps.getCenterY() > e.getY()) {
                    elps.setCenterY(e.getY());
                }

                gc.strokeOval(elps.getCenterX(), elps.getCenterY(), elps.getRadiusX(), elps.getRadiusY());
                gc.fillOval(elps.getCenterX(), elps.getCenterY(), elps.getRadiusX(), elps.getRadiusY());

                //undoHistory.push(new Ellipse(elps.getCenterX(), elps.getCenterY(), elps.getRadiusX(), elps.getRadiusY()));
            } else if (movbtn.isSelected()){
                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                //gc.drawImage(selimg, e.getX(), e.getY());
            } else if (tribtn.isSelected()){
                gc.drawImage(tmp_snap, 0, 0);
                triangle.getPoints().add(2, e.getX());
                triangle.getPoints().add(3, triangle.getPoints().get(1));
                triangle.getPoints().add(4, Math.abs((e.getX() - triangle.getPoints().get(0))));
                triangle.getPoints().add(5, e.getY());
                double[] arrx = {triangle.getPoints().get(0), triangle.getPoints().get(2), triangle.getPoints().get(4)};
                double[] arry = {triangle.getPoints().get(1), triangle.getPoints().get(3), triangle.getPoints().get(5)};
                gc.strokePolygon(arrx, arry, 3);
            } else if (polybtn.isSelected()){
                gc.drawImage(tmp_snap, 0, 0);
                double rad = Math.max(Math.abs(e.getX() - polygon.getStartX()), Math.abs(e.getY() - polygon.getStartY()));
                drawNPolygon(12, gc, polygon.getStartX(), polygon.getStartY(), rad);
                //drawNPolygon(6, gc, e.getX(), e.getY(), rad);
            }
        });

        canvas.setOnMouseReleased(e -> {
            menubar.setImageSaved(false);
            ////////////////////
            if (drowbtn.isSelected()) {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
                gc.closePath();
            } else if (rubberbtn.isSelected()) {
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            } else if (linebtn.isSelected()) {
                line.setEndX(e.getX());
                line.setEndY(e.getY());
                gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
                //window.removeToGroup(line);

                undoHistory.push(new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
            } else if (rectbtn.isSelected()) {
                rect.setWidth(Math.abs((e.getX() - rect.getX())));
                rect.setHeight(Math.abs((e.getY() - rect.getY())));
                //rect.setX((rect.getX() > e.getX()) ? e.getX(): rect.getX());
                if (rect.getX() > e.getX()) {
                    rect.setX(e.getX());
                }
                //rect.setY((rect.getY() > e.getY()) ? e.getY(): rect.getY());
                if (rect.getY() > e.getY()) {
                    rect.setY(e.getY());
                }

                gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

                undoHistory.push(new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()));

            } else if (circlebtn.isSelected()) {
                circ.setRadius((Math.abs(e.getX() - circ.getCenterX()) + Math.abs(e.getY() - circ.getCenterY())) / 2);

                if (circ.getCenterX() > e.getX()) {
                    circ.setCenterX(e.getX());
                }
                if (circ.getCenterY() > e.getY()) {
                    circ.setCenterY(e.getY());
                }

                gc.fillOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());
                gc.strokeOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());

                undoHistory.push(new Circle(circ.getCenterX(), circ.getCenterY(), circ.getRadius()));
            } else if (elpslebtn.isSelected()) {
                elps.setRadiusX(Math.abs(e.getX() - elps.getCenterX()));
                elps.setRadiusY(Math.abs(e.getY() - elps.getCenterY()));

                if (elps.getCenterX() > e.getX()) {
                    elps.setCenterX(e.getX());
                }
                if (elps.getCenterY() > e.getY()) {
                    elps.setCenterY(e.getY());
                }

                gc.strokeOval(elps.getCenterX(), elps.getCenterY(), elps.getRadiusX(), elps.getRadiusY());
                gc.fillOval(elps.getCenterX(), elps.getCenterY(), elps.getRadiusX(), elps.getRadiusY());

                undoHistory.push(new Ellipse(elps.getCenterX(), elps.getCenterY(), elps.getRadiusX(), elps.getRadiusY()));
            } else if (selbtn.isSelected()){
                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                selrect.setWidth(Math.abs((e.getX() - selrect.getX())));
                selrect.setHeight(Math.abs((e.getY() - selrect.getY())));
                //rect.setX((rect.getX() > e.getX()) ? e.getX(): rect.getX());
                if (selrect.getX() > e.getX()) {
                    selrect.setX(e.getX());
                }
                //rect.setY((rect.getY() > e.getY()) ? e.getY(): rect.getY());
                if (selrect.getY() > e.getY()) {
                    selrect.setY(e.getY());
                }

                WritableImage writableimage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                canvas.snapshot(null, writableimage);

                gc.fillRect(selrect.getX(), selrect.getY(), selrect.getWidth(), selrect.getHeight());

                PixelReader reader = writableimage.getPixelReader();

                selimg = new WritableImage(reader, (int)selrect.getX(), (int)selrect.getY(),
                        (int)(selrect.getWidth()), (int)(selrect.getHeight()));

            } else if (movbtn.isSelected()) {
                gc.drawImage(selimg, e.getX(), e.getY());
            } else if (tribtn.isSelected()){
                triangle.getPoints().add(2, e.getX());
                triangle.getPoints().add(3, triangle.getPoints().get(1));
                triangle.getPoints().add(4, Math.abs((e.getX() - triangle.getPoints().get(0))));
                triangle.getPoints().add(5, e.getY());
                double[] arrx = {triangle.getPoints().get(0), triangle.getPoints().get(2), triangle.getPoints().get(4)};
                double[] arry = {triangle.getPoints().get(1), triangle.getPoints().get(3), triangle.getPoints().get(5)};
                gc.strokePolygon(arrx, arry, 3);
                undoHistory.push(triangle);
            } else if (polybtn.isSelected()){
                //gc.drawImage(tmp_snap, 0, 0);
                double rad = Math.max(Math.abs(e.getX() - polygon.getStartX()), Math.abs(e.getY() - polygon.getStartY()));
                drawNPolygon(12, gc, polygon.getStartX(), polygon.getStartY(), rad);
                //drawNPolygon(6, gc, e.getX(), e.getY(), rad);
                undoHistory.push(polygon);
            }


            redoHistory.clear();
            Shape lastUndo = undoHistory.lastElement();
            lastUndo.setFill(gc.getFill());
            lastUndo.setStroke(gc.getStroke());
            lastUndo.setStrokeWidth(gc.getLineWidth());

            // color picker
            cpLine.setOnAction(ess -> gc.setStroke(cpLine.getValue()));
            cpFill.setOnAction(sdfe -> gc.setFill(cpFill.getValue()));

            // slider
            slider.valueProperty().addListener(dfe -> {
                double width = slider.getValue();
                if (textbtn.isSelected()) {
                    gc.setLineWidth(GRAPHICS_CONTEXT_LINE_WIDTH);
                    gc.setFont(Font.font(slider.getValue()));
                    line_width.setText(String.format("%.1f", width));
                    return;
                }
                line_width.setText(String.format("%.1f", width));
                gc.setLineWidth(width);
            });



            /*------- Undo & Redo ------*/
            // Undo
            undo.setOnAction(sdfe -> {
                if (!undoHistory.empty()) {
                    gc.clearRect(CANVAS_ORIGIN_X, CANVAS_ORIGIN_Y, canvas.getWidth(), canvas.getHeight());
                    gc.drawImage(img, CANVAS_ORIGIN_X, CANVAS_ORIGIN_Y);
                    Shape removedShape = undoHistory.lastElement();
                    if (removedShape.getClass() == Line.class) {
                        Line tempLine = (Line) removedShape;
                        tempLine.setFill(gc.getFill());
                        tempLine.setStroke(gc.getStroke());
                        tempLine.setStrokeWidth(gc.getLineWidth());
                        redoHistory.push(new Line(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY()));

                    } else if (removedShape.getClass() == Rectangle.class) {
                        Rectangle tempRect = (Rectangle) removedShape;
                        tempRect.setFill(gc.getFill());
                        tempRect.setStroke(gc.getStroke());
                        tempRect.setStrokeWidth(gc.getLineWidth());
                        redoHistory.push(new Rectangle(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight()));
                    } else if (removedShape.getClass() == Circle.class) {
                        Circle tempCirc = (Circle) removedShape;
                        tempCirc.setStrokeWidth(gc.getLineWidth());
                        tempCirc.setFill(gc.getFill());
                        tempCirc.setStroke(gc.getStroke());
                        redoHistory.push(new Circle(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius()));
                    } else if (removedShape.getClass() == Ellipse.class) {
                        Ellipse tempElps = (Ellipse) removedShape;
                        tempElps.setFill(gc.getFill());
                        tempElps.setStroke(gc.getStroke());
                        tempElps.setStrokeWidth(gc.getLineWidth());
                        redoHistory.push(new Ellipse(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY()));
                    }
                    Shape lastRedo = redoHistory.lastElement();
                    lastRedo.setFill(removedShape.getFill());
                    lastRedo.setStroke(removedShape.getStroke());
                    lastRedo.setStrokeWidth(removedShape.getStrokeWidth());
                    undoHistory.pop();

                    for (int i = 0; i < undoHistory.size(); i++) {
                        Shape shape = undoHistory.elementAt(i);
                        if (shape.getClass() == Line.class) {
                            Line temp = (Line) shape;
                            gc.setLineWidth(temp.getStrokeWidth());
                            gc.setStroke(temp.getStroke());
                            gc.setFill(temp.getFill());
                            gc.strokeLine(temp.getStartX(), temp.getStartY(), temp.getEndX(), temp.getEndY());
                        } else if (shape.getClass() == Rectangle.class) {
                            Rectangle temp = (Rectangle) shape;
                            gc.setLineWidth(temp.getStrokeWidth());
                            gc.setStroke(temp.getStroke());
                            gc.setFill(temp.getFill());
                            gc.fillRect(temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight());
                            gc.strokeRect(temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight());
                        } else if (shape.getClass() == Circle.class) {
                            Circle temp = (Circle) shape;
                            gc.setLineWidth(temp.getStrokeWidth());
                            gc.setStroke(temp.getStroke());
                            gc.setFill(temp.getFill());
                            gc.fillOval(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), temp.getRadius());
                            gc.strokeOval(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), temp.getRadius());
                        } else if (shape.getClass() == Ellipse.class) {
                            Ellipse temp = (Ellipse) shape;
                            gc.setLineWidth(temp.getStrokeWidth());
                            gc.setStroke(temp.getStroke());
                            gc.setFill(temp.getFill());
                            gc.fillOval(temp.getCenterX(), temp.getCenterY(), temp.getRadiusX(), temp.getRadiusY());
                            gc.strokeOval(temp.getCenterX(), temp.getCenterY(), temp.getRadiusX(), temp.getRadiusY());
                        }
                    }
                    //gc.drawImage(img, 0, 0);
                } else {
                    System.out.println("there is no action to undo");
                }
            });

            // Redo
            redo.setOnAction(es -> {
                if (!redoHistory.empty()) {
                    Shape shape = redoHistory.lastElement();
                    gc.setLineWidth(shape.getStrokeWidth());
                    gc.setStroke(shape.getStroke());
                    gc.setFill(shape.getFill());

                    redoHistory.pop();
                    if (shape.getClass() == Line.class) {
                        Line tempLine = (Line) shape;
                        gc.strokeLine(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY());
                        undoHistory.push(new Line(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY()));
                    } else if (shape.getClass() == Rectangle.class) {
                        Rectangle tempRect = (Rectangle) shape;
                        gc.fillRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());
                        gc.strokeRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());

                        undoHistory.push(new Rectangle(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight()));
                    } else if (shape.getClass() == Circle.class) {
                        Circle tempCirc = (Circle) shape;
                        gc.fillOval(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius(), tempCirc.getRadius());
                        gc.strokeOval(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius(), tempCirc.getRadius());

                        undoHistory.push(new Circle(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius()));
                    } else if (shape.getClass() == Ellipse.class) {
                        Ellipse tempElps = (Ellipse) shape;
                        gc.fillOval(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY());
                        gc.strokeOval(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY());

                        undoHistory.push(new Ellipse(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY()));
                    }
                    Shape lastUndos = undoHistory.lastElement();
                    lastUndos.setFill(gc.getFill());
                    lastUndos.setStroke(gc.getStroke());
                    lastUndos.setStrokeWidth(gc.getLineWidth());
                } else {
                    System.out.println("there is no action to redo");
                }
            });
        });
        //window.set_group(group);
        window.set_Canvas(canvas);
        return btns;
    }
}
