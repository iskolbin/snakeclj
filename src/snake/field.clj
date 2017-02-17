(ns snake.field)

(defn field [w h]
  {:width w
   :height h
   :cells (vec (make-array Object (* w h)))})

(defn- cell-index [field [x y]]
  (+ (* (field :height) x) y))

(defn get-cell [field xy]
  (field (cell-index state xy)))

(defn set-cell [state xy v]
  (set field (cell-index state xy) v))
