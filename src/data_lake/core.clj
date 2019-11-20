;map <F5> :wa<CR>:%Eval<CR>
;map <F6> :wa<CR>:!lein test<CR>
;map <F7> :wa<CR>:!lein run<CR>
(ns data-lake.core
  (:require [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [honeysql.helpers :as h]
            )
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn db-do-noexcept
  "Do cmd on db"
  [db cmd]
  (try (jdbc/db-do-commands db cmd) 
       (catch Exception e
              (println (.getMessage e)))));TODO: logging

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "database.db"
   })

(def cmd
  (jdbc/create-table-ddl 
    :file
    [[:hash      :INTEGER :PRIMARY :KEY :NOT :NULL]
     [:path      :TEXT]
     [:type      :TEXT]
     [:extension :TEXT]]))

;(db-do-noexcept db cmd)
#_(jdbc/insert! db :file {:id 20123 :path "p"
                        :type "int" :extension "ext"})

(jdbc/insert-multi! 
  db 
  :file
  [{:id 1 :path "1" :type "1" :extension "ext"}
   {:id 2 :path "1" :type "1" :extension "ext"}
   {:id 3 :path "1" :type "1" :extension "ext"}])

(java.util.UUID/randomUUID)
