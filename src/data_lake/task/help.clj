(ns data-lake.task.help
  (:require [data-lake.task.common :as tc]))

(def help-msg 
 "Usage:
 lein run help    Print this help msg.
 lein run init    Initiate system if not initiated.
 
 lein run sqlite new <name.db-path> <schema.edn-path>
   Create new empty sqlite db in <name.db-path> with schema from <schema.edn-path>

 lein run add image <target> [<source>]+
   Add <source> to <target>
   <source> can be: 
     image(jpg, png) file
     directory containing image(jpg, png) files
   <target> can be: 
     path of database
 ")
;; TODO: autogenerate above...

(defmethod tc/run-task "help" [task & args] 
  (print help-msg))
(defmethod tc/run-task nil [& args] 
  (print help-msg))
