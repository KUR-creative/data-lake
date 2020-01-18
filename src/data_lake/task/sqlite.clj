(ns data-lake.task.sqlite
  (:require [clojure.edn :as edn]
            [clojure.tools.trace :as dbg]
            [data-lake.core.sqlite :refer [create!]]
            [data-lake.task.common :as tc]
            ))


(defn run-cmd ;; TODO: refactor
  "Run command" 
  [& args]
  (cond 
    (= (first args) "new") 
    (let [[_ db-path edn-path] args
          schema (-> edn-path
                     slurp edn/read-string :schema)]
      ;(create! db-path schema))
      (create! db-path (dbg/trace schema)))

    :else false)) ;; Failure case

(defmethod tc/run-task "sqlite" [task & args] 
  (if (tc/initiated?)
      (do (when-not (contains? (set args) :no-log) 
            (tc/log args))
          (apply run-cmd args))
      (println "Please run `$lake init` first")))
