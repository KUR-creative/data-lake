(ns data-lake.task.sqlite
  (:require [clojure.edn :as edn]
            [clojure.tools.trace :as dbg]
            [data-lake.core.sqlite :refer [create!]]
            [data-lake.task.common :as tc]
            ))


(defmulti run-cmd (fn [& args] (when args (first args))))

(defmethod run-cmd "new" [& args]
  (let [[_ db-path edn-path] args
        schema (-> edn-path slurp edn/read-string :schema)]
    (create! db-path (dbg/trace schema))))

(defmethod run-cmd :default [& args] false)


(defmethod tc/run-task "sqlite" [task & args] 
  (if (tc/initiated?)
      (do (when-not (contains? (set args) :no-log) 
            (tc/log args))
          (apply run-cmd args))
      (println "Please run `$lake init` first")))
