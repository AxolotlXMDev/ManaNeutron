package org.a8043.gui.util;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class LightScratch {
        public static final List<Color> RAINBOW_COLORS = List.of(
                Color.RED, Color.ORANGE, Color.YELLOW,
                Color.GREEN, Color.BLUE, Color.INDIGO, Color.VIOLET
        );

        private final Labeled target;
        private final Timeline timeline;
        private final double cycleSeconds;
        private final List<Stop> stops;

        public LightScratch(Labeled target, List<Color> colors, double cycleSeconds) {
                this.target = target;
                this.cycleSeconds = cycleSeconds;

                stops = new ArrayList<>();
                int n = colors.size();
                for (int i = 0; i < n; i++) {
                        double offset = (n == 1) ? 0 : (double) i / (n - 1);
                        stops.add(new Stop(offset, colors.get(i)));
                }

                timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> updateGradient()));
                timeline.setCycleCount(Animation.INDEFINITE);
        }

        private void updateGradient() {
                double phase = (System.currentTimeMillis() / (cycleSeconds * 1000.0)) % 1.0;

                LinearGradient gradient = new LinearGradient(
                        phase, 0, phase + 1, 0,
                        true,
                        CycleMethod.REPEAT,
                        stops
                );

                target.setTextFill(gradient);
        }

        public void start() {
                timeline.play();
        }

        public void stop() {
                timeline.stop();
        }
}
