(ns drawing.utils.thread-local
  (:import (clojure.lang IDeref)))
(defn thread-local*
  "Thread local storage clojure impl, taken from useful:
   https://github.com/flatland/useful/blob/develop/src/flatland/useful/utils.clj"
  [init]
  (let [generator (proxy [ThreadLocal] []
                    (initialValue [] (init)))]
    (reify IDeref
      (deref [this]
        (.get generator)))))

(defmacro thread-local
  [& body]
  `(thread-local* (fn [] ~@body)))