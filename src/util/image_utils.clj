(ns util.image-utils
  (:require [clojure.java.io :as io]
            [data-lake.consts :as c])
  (:import (javax.imageio ImageIO)
           (com.drew.imaging ImageMetadataReader)
           (com.drew.imaging FileTypeDetector)
           (com.drew.imaging FileType)

           (com.drew.imaging.png PngMetadataReader)
           (com.drew.metadata.png PngDirectory)

           (com.drew.imaging.jpeg JpegMetadataReader)
           (com.drew.metadata.jpeg JpegDirectory)

           (com.github.kilianB.hashAlgorithms PerceptiveHash)))

(def hasher (PerceptiveHash. c/image-hash-length))
(defn img-hash [path]
  "PerceptiveHash of image loaded from path. 
  length of hash is consts/image-hash-length
  Use .normalizedHammingDistance to calc distance of 2 hash"
  (.hash hasher (io/file path)))
;(print (.normalizedHammingDistance hash1 hash1))

(defn- private-type [path]
  (-> path 
      io/input-stream 
      FileTypeDetector/detectFileType))

(defn img-type [path] 
  "Image type from metadata."
  (str (private-type path)))

;; TODO: refactoring.. how? 
;; TODO: Add fixture and test cases.
;(def path (io/file "./DATA/very_big.jpg"))
;(def path (io/file "./DATA/big.png"))
(defmulti width (fn [path] (private-type path)))
(defmethod width FileType/Jpeg [path]
  (-> (io/file path)
      (JpegMetadataReader/readMetadata)
      (.getFirstDirectoryOfType JpegDirectory)
      (.getInt JpegDirectory/TAG_IMAGE_WIDTH)))
(defmethod width FileType/Png [path]
  (-> (io/file path)
      (PngMetadataReader/readMetadata)
      (.getFirstDirectoryOfType PngDirectory)
      (.getInt PngDirectory/TAG_IMAGE_WIDTH)))

(defmulti height (fn [path] (private-type path)))
(defmethod height FileType/Jpeg [path]
  (-> (io/file path)
      (JpegMetadataReader/readMetadata)
      (.getFirstDirectoryOfType JpegDirectory)
      (.getInt JpegDirectory/TAG_IMAGE_HEIGHT)))
(defmethod height FileType/Png [path]
  (-> (io/file path)
      (PngMetadataReader/readMetadata)
      (.getFirstDirectoryOfType PngDirectory)
      (.getInt PngDirectory/TAG_IMAGE_HEIGHT)))
