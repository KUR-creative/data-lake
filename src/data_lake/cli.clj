(ns data-lake.cli
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]
            [java-time :refer [offset-date-time]]
            [data-lake.task.sqlite :as sqlite]
            [data-lake.task.init :as init]
            [data-lake.consts :refer :all]
            ))

(defn initiated? [] 
  (.exists (io/as-file history-db-path)))
(defn log [args]
  (if (.exists (io/as-file history-db-path))
      (jdbc/insert! (sqlite/db-spec history-db-path)
                    :history 
                    {:cmd  (clojure.string/join " " args)
                     :time (offset-date-time)})))

(def help-msg "help-msg\n")
(def no-need-init-msg "Already initiated")

(defmulti run-task 
  (fn [& args] (when args (first args))))
(defmethod run-task "help" [task & args] 
  (print help-msg))
(defmethod run-task nil [& args] 
  (print help-msg))

(defn run
  "Run command according to args"
  [& args]
  (if (= (count args) 0)
      (print help-msg)
      (let [task (first args)]
        (cond 
          (= task "help") 
          (print help-msg)

          (= task "init") 
          (if (initiated?)
              (print no-need-init-msg) 
              (do
                (sqlite/create! 
                  history-db-path 
                  (sqlite/schema-map history-schema-path))
                (log args)
                (println "System is initiated. History DB is created.")))

          (= task "sqlite") 
          (if (initiated?)
              (do (when-not (contains? (set args) :no-log) 
                    (log args))
                  (apply sqlite/run-cmd (rest args)))
              (println "Please run `$lake init` first"))
      ))))