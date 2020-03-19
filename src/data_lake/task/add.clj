(ns data-lake.task.add
  (:require [clojure.edn :as edn]
            [data-lake.task.common :as tc]
            ))

(defmethod tc/run-task "add" [task & args]
  (println task args))
