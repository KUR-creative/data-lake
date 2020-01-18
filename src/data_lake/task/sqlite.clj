(ns data-lake.task.sqlite
  (:require [clojure.edn :as edn]
            [clojure.tools.trace :as dbg]
            [data-lake.core.sqlite :refer [create!]]
            ))


(defn run-cmd 
  "Run command"
  [& args]
  (vec args)
  (cond 
    (= (first args) "new") 
    (let [[_ db-path edn-path] args
          schema (-> edn-path
                     slurp edn/read-string :schema)]
      ;(create! db-path schema))
      (create! db-path (dbg/trace schema)))

    :else false)) ;; Failure case
