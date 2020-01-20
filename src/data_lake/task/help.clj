(ns data-lake.task.help
  (:require [data-lake.task.common :as tc]))

(def help-msg "help-msg\n")

(defmethod tc/run-task "help" [task & args] 
  (print help-msg))
(defmethod tc/run-task nil [& args] 
  (print help-msg))
