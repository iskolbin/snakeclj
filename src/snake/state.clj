(ns snake.state
  (:require [snake.field
             snake.snake]))

(defn state []
  {:player (snake/snake [[0 0]])
   :score 0
   :state :playing
   :apples []
   :apple-spawn-rate 1/3
   :apple-spawn-counter 0})

(defn overlaps-apple? [state]
  (snake/overlaps-vector? (state :player) (state :apples)))

(defn set-status [state status]
  (assoc state :status status))

(defn do-spawn-apple [state]
  (conj apples (
  (let [field (state :field)]
    (assoc state :field (set field (rand-int (count field) "a")))))

(defn spawn-apple [state dt]
  (let [apple-spawn-counter (+ (state :apple-spawn-counter) dt)]
    (if (>= apple-spawn-counter (state :apple-spawn-rate))
      (-> state
        (assoc :apple-spawn-counter 0)
        (do-spawn-apple))
      (-> state
        (assoc :apple-spawn-counter apple-spawn-counter)))))

(defn update [state dt]
  (if (= (state :state) :playing)
    (-> state (spawn-apple dt) (player-move dt))))
