(ns data-lake.cli
  (:require [data-lake.task.common :as tc]
            [data-lake.task.init]
            [data-lake.task.sqlite]
            [data-lake.task.help]
            [data-lake.task.add]
            ))

(defn run
  "Run command according to args"
  [& args]
  (apply tc/run-task args))
