(ns data-lake.core.add1-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]

            [honeysql.core :as sql]
            [honeysql.helpers :as h]

            [data-lake.core.add1 :as a1]
            [data-lake.core.sqlite :as s]
            ))

(deftest add1-test
  (let [db-path  "./test/fixture/add1.db"
        edn-path "./DB/sqlite/szmc_schema_0.1.0.edn"
        db       (s/create! db-path 
                            (s/schema-map edn-path))
        jpg-path "./DATA/manga109/images/AisazuNihaIrarenai/AisazuNihaIrarenai_000.jpg"]
    (testing "Add just 1 image"
      (a1/add! db jpg-path :rgb :manga109)
      (let [query (-> (h/select :*) (h/from :file) sql/format)
            row   (first (jdbc/query db query))]
        (is (= jpg-path (row :path)))
        (is (= "jpg" (row :extension)))))

    (.delete (io/as-file db-path))))
