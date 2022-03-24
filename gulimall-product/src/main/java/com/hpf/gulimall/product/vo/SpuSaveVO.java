/**
  * Copyright 2022 bejson.com 
  */
package com.hpf.gulimall.product.vo;
import java.util.List;

/**
 * Auto-generated: 2022-03-24 22:21:53
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class SpuSaveVO {

    private String spuName;
    private String spuDescription;
    private int catelogId;
    private int brandId;
    private double weight;
    private int publishStatus;
    private List<String> decript;
    private List<Images> images;
    private Bounds bounds;
    private List<BaseAttrs> baseAttrs;
    private List<Skus> skus;
    public void setSpuName(String spuName) {
         this.spuName = spuName;
     }
     public String getSpuName() {
         return spuName;
     }

    public void setSpuDescription(String spuDescription) {
         this.spuDescription = spuDescription;
     }
     public String getSpuDescription() {
         return spuDescription;
     }

    public void setCatelogId(int catelogId) {
         this.catelogId = catelogId;
     }
     public int getCatelogId() {
         return catelogId;
     }

    public void setBrandId(int brandId) {
         this.brandId = brandId;
     }
     public int getBrandId() {
         return brandId;
     }

    public void setWeight(double weight) {
         this.weight = weight;
     }
     public double getWeight() {
         return weight;
     }

    public void setPublishStatus(int publishStatus) {
         this.publishStatus = publishStatus;
     }
     public int getPublishStatus() {
         return publishStatus;
     }

    public void setDecript(List<String> decript) {
         this.decript = decript;
     }
     public List<String> getDecript() {
         return decript;
     }

    public void setImages(List<Images> images) {
         this.images = images;
     }
     public List<Images> getImages() {
         return images;
     }

    public void setBounds(Bounds bounds) {
         this.bounds = bounds;
     }
     public Bounds getBounds() {
         return bounds;
     }

    public void setBaseAttrs(List<BaseAttrs> baseAttrs) {
         this.baseAttrs = baseAttrs;
     }
     public List<BaseAttrs> getBaseAttrs() {
         return baseAttrs;
     }

    public void setSkus(List<Skus> skus) {
         this.skus = skus;
     }
     public List<Skus> getSkus() {
         return skus;
     }

}