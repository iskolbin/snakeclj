(ns snake.snake)

(def *directions*
  {:left  [-1  0]
   :right [ 1  0]
   :up    [ 0 -1]
   :down  [ 0  1]})

(defn snake [head]
  {:head head
   :tail (reduce conj clojure.lang.PersistentQueue/EMPTY [])
   :direction :right
   :move-counter 0
   :move-rate 0.5})

(defn head [snake]
  (first (snake :body)))

(defn overlaps-vector? [snake v]
  (some #{(snake :head)} v))

(defn overlaps-self? [snake]
  (overlaps-vector? (snake :tail)))

(defn- move [snake]
  (let [[x y] (head snake)
        [dx dy] (*directions* (snake :direction))
        counter (snake :move-counter)]
    (-> snake
      (assoc :body (pop (conj (snake :body) (snake :head))))
      (assoc :head [(+ x dx) (+ y dy)])
      (assoc :move-counter (- counter (snake :move-rate))))))

(defn tick [snake dt]
  (let [counter (+ (snake :move-counter) dt)]
    (if (> counter (snake :move-rate))
      (move snake)
      (assoc snake :move-counter counter))))
