(ns data-lake.core.add1
  (:require [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]
            [util.file-utils :as fu]
            [util.image-utils :as iu]
            ))

(defn add!
  "Add 1 image into db.
  If image size is too big, It doesn't calc img hash."
  [db img-path color category]
  (let [id (java.util.UUID/randomUUID)]
    (jdbc/insert! 
      db 
      :file
      {:guid      id
       :path      img-path
       :extension (fu/ext-without-dot img-path)
       :size      (.length (io/file img-path))})
    (jdbc/insert! 
      db 
      :image
      {:guid     id
       :hash     (iu/img-hash img-path) ;; TODO: Deal with too big image
       :type     (iu/img-type img-path) ;; TODO: img / not img / broken header
       :height   (iu/height img-path) 
       :width    (iu/width img-path)
       :color    color ;TODO  change
       :category category}))) ;TODO  change
