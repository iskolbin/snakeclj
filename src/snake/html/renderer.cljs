(ns snake.html.renderer)

(defn render-tile!
  [context state x y]
  (set! (. context -fillStyle) "rgb(0,1,0)")
  (.fillRect surface (* x 32) (* y 32)))

(defn render-snake!
  [context state]
  (let [snake (state :player)]
    (set! (. context -fillStyle) "rgb(0,0,1)")
    (map #(.fillRect context (* %0 32) (* %1 32)) (snake :tail))
    (set! (. context -fillStyle) "rgb(1,1,0)")
    (.fillRect context (* (snake :head 0) 32) (* (snake :head 1) 32))))

(defn render-apples!
  [context state]
  (set! (. context -fillStyle) "rgb(1,0,0)")
  (map #(.fillRect context (* %0 32) (* %1 32)) (state :apples)))

(defn render-floor!
  [context state]
  (loop [x 0]
    (if (< x (state :width))
      (do
        (loop [y 0]
          (if (< y (state :height))
            (do
              (render-tile! context state x y)
              (recur (inc y)))))
        (recur (inc x))))))

(defn render!
  [context state]
  (do
    (render-floor! context state)
    (render-snake! context state)
    (render-apples! context state)))
