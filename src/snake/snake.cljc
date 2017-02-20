(ns snake.snake)

(def ^:const directions
  {:left  [-1  0]
   :right [ 1  0]
   :up    [ 0 -1]
   :down  [ 0  1]})

(defn snake
  [head]
  {:head head
   :tail clojure.lang.PersistentQueue/EMPTY
   :direction :right
   :fed false
   :move-counter 0
   :move-rate 0.5})

(defn overlaps-vector?
  [snake v]
  (some #{(snake :head)} v))

(defn overlaps-self?
  [snake]
  (overlaps-vector? snake (snake :tail)))

(defn- move
  [snake]
  (let [[x y] (snake :head)
        [dx dy] (directions (snake :direction))
        tail (snake :tail)
        counter (snake :move-counter)]
    (-> snake
      (assoc :tail (if (snake :fed)
                     (conj tail (snake :head))
                     (pop (conj tail (snake :head)))))
      (assoc :fed false)
      (assoc :head [(+ x dx) (+ y dy)])
      (assoc :move-counter (- counter (snake :move-rate))))))

(defn feed
  [snake]
  (assoc snake :fed true))

(defn tick
  [snake dt]
  (let [counter (+ (snake :move-counter) dt)]
    (if (> counter (snake :move-rate))
      (move snake)
      (assoc snake :move-counter counter))))
