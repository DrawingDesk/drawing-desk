(ns drawing.utils.clock)

(def room-counters (agent {}))

(defn- -create-new-room-clock [clocks room-id]
  (println "Create new")
  (if (not (contains? clocks room-id))
    (assoc clocks room-id (atom 0))))

(defn- -get-or-create-room-clock [room-id]
  "Function return or create new vector clock for room"
  (if (not (contains? @room-counters room-id))
    (send room-counters -create-new-room-clock room-id))
  (await room-counters)
  (get @room-counters room-id))


(defn next-value [room-id]
  "Function returns next Lamport clock value"
  (println "Mocked func")
  (swap! (-get-or-create-room-clock room-id) inc))