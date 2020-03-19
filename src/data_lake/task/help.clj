(ns data-lake.task.help
  (:require [data-lake.task.common :as tc]))

(def help-msg 
 "Usage:
 lein run help    Print this help msg.
 lein run init    Initiate system if not initiated.
 
 lein run sqlite new <name.db-path> <schema.edn-path>
   Create new empty sqlite db in <name.db-path> with schema from <schema.edn-path>
 ")
;; TODO: autogenerate above...

(defmethod tc/run-task "help" [task & args] 
  (print help-msg))
(defmethod tc/run-task nil [& args] 
  (print help-msg))
