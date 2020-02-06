(ns data-lake.core.sqlite-test
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [orchestra.spec.test :as st]
            [honeysql.core :as sql]
            [honeysql.helpers :as h]
            [data-lake.core.sqlite :refer :all]))


(st/instrument)

(def schema
  {:file
   [[:guid      :BLOB    "NOT NULL" "PRIMARY KEY"]
    [:path      :TEXT    "NOT NULL"]
    [:extension :TEXT    "NOT NULL"]
    [:size      :INTEGER "NOT NULL"]]
   :ref_test
   [[:ref :BLOB "NOT NULL"
     :REFERENCES "file(guid)"]]
   :annotation
   [[:input  :BLOB "NOT NULL"] ;guid
    [:output :BLOB "NOT NULL"] ;guid
    [:method :TEXT "NOT NULL" 
     :REFERENCES "annotation_method(name)"]]})

(deftest create!-test
  (testing "create sqlite db file"
    (let [path "./test/fixture/test.db" 
          file (io/as-file path)
          _    (create! path {:file [[:test :INTEGER]
                                     [:chk  :BLOB]]})]
      (do (is (.exists file)) (.delete file))))
  (testing "create sqlite db file"
    (let [path "./test/fixture/test2.db" 
          file (io/as-file path)
          _    (create! path schema)]
      (do (is (.exists file)) 
          (.delete file))))
  (testing "if successful create! db then return db-spec"
    (let [path "./test/fixture/test3.db" 
          file (io/as-file path)
          db   (create! path schema)]
      (do (is (.exists file)) 
          (is (= {:classname   "org.sqlite.JDBC"
                  :subprotocol "sqlite"
                  :subname     path}
                 db))
          (.delete file)))))

(deftest create!-io-test
  (testing "insert and get"
    (let [path "./test/fixture/test4.db" 
          file (io/as-file path)
          db   (create! path schema)
          row  #(identity {:guid (java.util.UUID/randomUUID)
                           :path "test"
                           :extension "png"
                           :size 123})
          rows (vec (repeatedly 3 row))
          _    (doseq [row rows] 
                 (jdbc/insert! db :file row))
          out  (jdbc/query db (-> (h/select :guid)
                                  (h/from :file)
                                  sql/format))] 
      (do (is (= (into #{} (map :guid rows))
                 (into #{} (map #(java.util.UUID/fromString (:guid %)) out))
                 #_(into #() (map :guid out))))
          (.delete file)))))

(deftest szmc-sqlite-v0.1.0-schema-test
  (testing "create szmc v0.1.0 sqlite db from edn"
    (let [path     "./test/fixture/test5.db" 
          file     (io/as-file path)
          edn-path "./DB/sqlite/szmc_schema_0.1.0.edn"
          _        (create! path (schema-map edn-path))]
      (do (is (.exists file)) (.delete file)))))

;; TODO: test with running sql command(insert ...)
