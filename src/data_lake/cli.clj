(ns data-lake.cli
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]
            [java-time :refer [offset-date-time]]
            [data-lake.consts :as c]
            [data-lake.core.sqlite :as sqlite]
            [data-lake.task.common :as tc]
            [data-lake.task.init]
            [data-lake.task.sqlite]))


(def help-msg "help-msg\n")

(defmethod tc/run-task "help" [task & args] 
  (print help-msg))
(defmethod tc/run-task nil [& args] 
  (print help-msg))

(defn run
  "Run command according to args"
  [& args]
  (apply tc/run-task args))
