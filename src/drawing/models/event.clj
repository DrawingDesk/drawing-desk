(ns drawing.models.event)

(defrecord Event [sync-id user-id tag method args])