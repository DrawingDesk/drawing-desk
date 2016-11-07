(ns drawing.utils.clock)

(def room-counters (agent {}))

(defn next-value [room-id]
  "Function returns next Lamport clock value"
  (println "Mocked func")
  (+ 0 1))