(ns snake.state
  (:require [snake.snake :as snake]))

(def ^:const statuses
  #{:playing :lose :menu-paused :menu})


(defn state
  []
  {:player (snake/snake [0 0])
   :score 0
   :state :playing
   :width 40
   :height 20
   :apples []
   :apple-spawn-rate 1/3
   :apple-spawn-counter 0})

(defn overlaps-apple?
  [state]
  (snake/overlaps-vector? (state :player) (state :apples)))

(defn set-status
  [state status]
  (assoc state :status status))

(defn empty-cell?
  [state xy]
  (and (not= xy (state :player :head))
       (not (some #{xy} (state :player :tail)))
       (not (some #{xy} (state :apples)))))

(defn random-cell
  [state]
  [(rand-int (state :width)) (rand-int (state :height))])

(defn random-empty-cell
  [state]
  (loop [xy (random-cell state)]
    (if (empty-cell? state xy)
      xy
      (recur (random-cell state)))))

(defn do-spawn-apple
  [state]
  (let [apples (state :apples)
        empty-cell (random-empty-cell state)]
    (assoc state :apples (conj apples empty-cell))))

(defn- spawn-apple
  [state dt]
  (let [apple-spawn-counter (+ (state :apple-spawn-counter) dt)]
    (if (>= apple-spawn-counter (state :apple-spawn-rate))
      (-> state
        (assoc :apple-spawn-counter 0)
        (do-spawn-apple))
      (-> state
        (assoc :apple-spawn-counter apple-spawn-counter)))))

(defn- move-player
  [state dt]
  (assoc state :player (snake/tick (state :player) dt)))

(defn- check-collisions
  [state]
  (let [snake (state :player)]
    (if (snake/overlaps-self? snake)
      (set-status :lose)
      (let [apple (some #{(snake :head)} (state :apples))]
        (if (nil? apple)
          state
          (assoc state :player (snake/feed snake)))))))

(defn tick
  [state dt]
  (if (= (state :state) :playing)
    (-> state
      (spawn-apple dt)
      (move-player dt)
      (check-collisions))))
