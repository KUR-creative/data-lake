(ns data-lake.task.init
  (:require [clojure.java.io :as io]
            [data-lake.consts :as c]
            [data-lake.core.sqlite :as sqlite]
            [data-lake.task.common :as tc]
            ))

(def no-need-init-msg "Already initiated")

(defmethod tc/run-task "init" [& args] 
  (if (tc/initiated?)
    (print no-need-init-msg) 
    (do 
      (sqlite/create! 
        c/history-db-path 
        (sqlite/schema-map c/history-schema-path))
      (tc/log args)
      (println "System is initiated. History DB is created."))))

