package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO implements Serializable {

    private Long id;

    private String name;

    private String code;

    private String unit;

    private String description;

    private Float weight;

    private Float height;

    private Float width;

    private Float length;

    private String imageUrl;

    private Integer type;

    private String color;

    private Float cbm;

    private Float price;

    private Integer construction;

    private Integer masterPackingQty;

    private Integer masterNestCode;

    private Integer innerQty;

    private Integer packSize;

    private String nestCode;

    private String countryOfOrigin;

    private String vendorName;

    private Integer numberOfSet;

    private Float priceFOB;

    private Integer qty40HC;

    private Integer d57Qty;

    private String category;

    private String labels;

    private String planningNotes;

    private String factTag;

    private Float packagingLength;

    private Float packagingHeight;

    private Float packagingWidth;

    private ProductDTO setIdProduct;

    private ProductDTO parentProduct;

    private MaterialDTO material;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Float getCbm() {
        return cbm;
    }

    public void setCbm(Float cbm) {
        this.cbm = cbm;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getConstruction() {
        return construction;
    }

    public void setConstruction(Integer construction) {
        this.construction = construction;
    }

    public Integer getMasterPackingQty() {
        return masterPackingQty;
    }

    public void setMasterPackingQty(Integer masterPackingQty) {
        this.masterPackingQty = masterPackingQty;
    }

    public Integer getMasterNestCode() {
        return masterNestCode;
    }

    public void setMasterNestCode(Integer masterNestCode) {
        this.masterNestCode = masterNestCode;
    }

    public Integer getInnerQty() {
        return innerQty;
    }

    public void setInnerQty(Integer innerQty) {
        this.innerQty = innerQty;
    }

    public Integer getPackSize() {
        return packSize;
    }

    public void setPackSize(Integer packSize) {
        this.packSize = packSize;
    }

    public String getNestCode() {
        return nestCode;
    }

    public void setNestCode(String nestCode) {
        this.nestCode = nestCode;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Integer getNumberOfSet() {
        return numberOfSet;
    }

    public void setNumberOfSet(Integer numberOfSet) {
        this.numberOfSet = numberOfSet;
    }

    public Float getPriceFOB() {
        return priceFOB;
    }

    public void setPriceFOB(Float priceFOB) {
        this.priceFOB = priceFOB;
    }

    public Integer getQty40HC() {
        return qty40HC;
    }

    public void setQty40HC(Integer qty40HC) {
        this.qty40HC = qty40HC;
    }

    public Integer getd57Qty() {
        return d57Qty;
    }

    public void setd57Qty(Integer d57Qty) {
        this.d57Qty = d57Qty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getPlanningNotes() {
        return planningNotes;
    }

    public void setPlanningNotes(String planningNotes) {
        this.planningNotes = planningNotes;
    }

    public String getFactTag() {
        return factTag;
    }

    public void setFactTag(String factTag) {
        this.factTag = factTag;
    }

    public Float getPackagingLength() {
        return packagingLength;
    }

    public void setPackagingLength(Float packagingLength) {
        this.packagingLength = packagingLength;
    }

    public Float getPackagingHeight() {
        return packagingHeight;
    }

    public void setPackagingHeight(Float packagingHeight) {
        this.packagingHeight = packagingHeight;
    }

    public Float getPackagingWidth() {
        return packagingWidth;
    }

    public void setPackagingWidth(Float packagingWidth) {
        this.packagingWidth = packagingWidth;
    }

    public ProductDTO getSetIdProduct() {
        return setIdProduct;
    }

    public void setSetIdProduct(ProductDTO setIdProduct) {
        this.setIdProduct = setIdProduct;
    }

    public ProductDTO getParentProduct() {
        return parentProduct;
    }

    public void setParentProduct(ProductDTO parentProduct) {
        this.parentProduct = parentProduct;
    }

    public MaterialDTO getMaterial() {
        return material;
    }

    public void setMaterial(MaterialDTO material) {
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", unit='" + getUnit() + "'" +
            ", description='" + getDescription() + "'" +
            ", weight=" + getWeight() +
            ", height=" + getHeight() +
            ", width=" + getWidth() +
            ", length=" + getLength() +
            ", imageUrl='" + getImageUrl() + "'" +
            ", type=" + getType() +
            ", color='" + getColor() + "'" +
            ", cbm=" + getCbm() +
            ", price=" + getPrice() +
            ", construction=" + getConstruction() +
            ", masterPackingQty=" + getMasterPackingQty() +
            ", masterNestCode=" + getMasterNestCode() +
            ", innerQty=" + getInnerQty() +
            ", packSize=" + getPackSize() +
            ", nestCode='" + getNestCode() + "'" +
            ", countryOfOrigin='" + getCountryOfOrigin() + "'" +
            ", vendorName='" + getVendorName() + "'" +
            ", numberOfSet=" + getNumberOfSet() +
            ", priceFOB=" + getPriceFOB() +
            ", qty40HC=" + getQty40HC() +
            ", d57Qty=" + getd57Qty() +
            ", category='" + getCategory() + "'" +
            ", labels='" + getLabels() + "'" +
            ", planningNotes='" + getPlanningNotes() + "'" +
            ", factTag='" + getFactTag() + "'" +
            ", packagingLength=" + getPackagingLength() +
            ", packagingHeight=" + getPackagingHeight() +
            ", packagingWidth=" + getPackagingWidth() +
            ", setIdProduct=" + getSetIdProduct() +
            ", parentProduct=" + getParentProduct() +
            ", material=" + getMaterial() +
            "}";
    }
}
