(ns snake)

(defn state []
  {:player-head [3 0]
   :player-tail [0 0]
   :player-direction :right
   :field-width 20
   :field-height 20
   :score 0
   :state :playing
   :field (vec (make-array Object 400))
   :apple-spawn-rate 1/3
   :apple-spawn-counter 0
   :player-move-rate 1
   :player-move-counter 0})

(defn cell-index [state [x y]]
  (+ (* (state :field-height) x) y))

(defn get-cell [state xy]
  ((state :field) (cell-index state xy)))

(defn set-cell [state xy v]
  (assoc state :field (set (state :field) (cell-index state xy) v)))

(defn player-overlaps-self? [state]
  (= (get-cell state (state :player-head) "@")))

(defn player-overlaps-apple? [state]
  (= (get-cell state (state :player-head) "a")))

(defn do-collect-apple [state]
  (set-cell state (state :player-head) "@"))

(defn set-status [state status]
  (assoc state :status status))

(defn do-player-move [state]
  (cond
    (player-overlaps-apple? state) (do-collect-apple state)
    (player-overlaps-self? state) (set-status state :lose)
    (:else status)))

(defn player-move [state dt]
  (let [player-move-counter (+ (state :player-move-counter) dt)]
    (if (>= player-move-counter (state :player-move-rate))
      (-> state
        (assoc :player-move-counter 0)
        (do-player-move))
      (-> state
        (assoc :player-move-counter player-move-counter)))))

(defn do-spawn-apple [state]
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
