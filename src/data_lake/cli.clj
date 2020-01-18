(ns data-lake.cli
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]
            [java-time :refer [offset-date-time]]
            [data-lake.core.sqlite :as sqlite]
            [data-lake.consts :as c]

            [data-lake.task.common :as tc]
            [data-lake.task.init]
            [data-lake.task.sqlite]
            ;[data-lake.task.sqlite :refer [run-cmd]]
            ))


(def help-msg "help-msg\n")
(def no-need-init-msg "Already initiated")

(defmethod tc/run-task "help" [task & args] 
  (print help-msg))
(defmethod tc/run-task nil [& args] 
  (print help-msg))

(defn run
  "Run command according to args"
  [& args]
  (apply tc/run-task args))
