(ns drawing.utils.clock
  (:use [drawing.data.events-dao :as events-dao]))

(def room-counters (agent {}))

(defn- create-new-room-clock [clocks room-id]
  (if (not (contains? clocks room-id))
    (let [last-event (events-dao/get-last-event room-id)]
      (assoc clocks room-id (atom (if (nil? (:sync-id last-event)) 0 (:sync-id last-event)))))))

(defn- get-or-create-room-clock [room-id]
  "Function return or create new vector clock for room"
  (if (not (contains? @room-counters room-id))
    (send room-counters create-new-room-clock room-id))
  (await room-counters)
  (get @room-counters room-id))


(defn next-value [room-id]
  "Function returns next Lamport clock value"
  (swap! (get-or-create-room-clock room-id) inc))