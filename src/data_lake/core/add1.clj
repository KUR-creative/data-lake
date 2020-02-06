(ns data-lake.core.add1
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]
            [util.file-utils :as fu]
            ))

(defn add!
  "Add 1 image into db"
  [db img-path]
  (jdbc/insert! db 
                :file
                {:guid      (java.util.UUID/randomUUID)
                 :path      img-path
                 :extension (fu/ext-without-dot img-path)
                 :size      (.length (io/file img-path))
                 }))
