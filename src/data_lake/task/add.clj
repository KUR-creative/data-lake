(ns data-lake.task.add
  (:require [clojure.edn :as edn]
            [data-lake.task.common :as tc]
            [data-lake.core.sqlite :as cs]
            [data-lake.core.add1 :as a1]
            ))

(defmethod tc/run-task "add" [task & args]
  (when (empty? args)
    (throw (IllegalArgumentException. "wrong args")))
  (when-not (contains? (set args) :no-log) 
    (tc/log args))
  (let [[target & sources] args]
    (a1/add! (cs/db-spec target)
             (first sources) ; TODO
             :tmp-color
             :tmp-category)))
