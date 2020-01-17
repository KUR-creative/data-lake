;map <F5> :wa<CR>:%Eval<CR>
;map <F6> :wa<CR>:!lein test<CR>
;map lr :wa<CR>:!lein run 
(ns data-lake.main
  (:require [util.db-util :as dbu]
            [clojure.java.jdbc :as jdbc]
            [clojure.java.io :as io]
            [data-lake.task.sqlite :as sqlite]
            [java-time :refer [offset-date-time]]
            )
  (:gen-class))

(def history-db-path "./DB/cmd_history.db")
(def history-schema-path "./DB/sqlite/cmd_history_0.1.0.edn")
(defn initiated? [] 
  (.exists (io/as-file history-db-path)))
(defn log [args]
  (if (.exists (io/as-file history-db-path))
      (jdbc/insert! (sqlite/db-spec history-db-path)
                    :history 
                    {:cmd  (clojure.string/join " " args)
                     :time (offset-date-time)})))

(def help-msg "help-msg\n")
(defn -main
  "Entry point"
  [& args]
  (if (= (count args) 0)
      (print help-msg)
      (let [task (first args)]
        (cond 
          (= task "help") 
          (print help-msg)

          (= task "init") 
          (if (initiated?)
              (println "Already initiated") 
              (sqlite/create! 
                history-db-path 
                (sqlite/schema-map history-schema-path)))

          (= task "sqlite") 
          (if (initiated?)
              (do (when-not (contains? args :no-log) 
                    (log args))
                  (apply sqlite/run-cmd (rest args)))
              (println "Please run `$lake init` first"))
      ))))
