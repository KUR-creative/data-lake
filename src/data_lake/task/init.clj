(ns data-lake.task.init
  (:require [clojure.java.io :as io]
            [data-lake.task.sqlite :as sqlite]
            [data-lake.consts :refer :all]
            ;[data-lake.task.common :refer [run-task]]
            ))

#_(def no-need-init-msg "Already initiated")

#_(defmethod run-task "init" [task & args] 
  (= task "init") 
  (if (initiated?)
    (print no-need-init-msg) 
    (do 
      (sqlite/create! 
        history-db-path 
        (sqlite/schema-map history-schema-path))
      (log args)
      (println "System is initiated. History DB is created."))))
