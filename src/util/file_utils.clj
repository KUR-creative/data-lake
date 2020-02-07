(ns util.file-utils
  (:require [clojure.java.io :as io]
            [me.raynes.fs :as fs]
            ;[clojure.spec.alpha :as s]
            )
  (:import (com.drew.metadata.file FileTypeDirectory)
           ))

(defn ext-without-dot [path]
  (-> path fs/extension rest clojure.string/join))
