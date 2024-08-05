package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "unit")
    private String unit;

    @Column(name = "description")
    private String description;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "height")
    private Float height;

    @Column(name = "width")
    private Float width;

    @Column(name = "length")
    private Float length;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "type")
    private Integer type;

    @Column(name = "color")
    private String color;

    @Column(name = "cbm")
    private Float cbm;

    @Column(name = "price")
    private Float price;

    @Column(name = "construction")
    private Integer construction;

    @Column(name = "master_packing_qty")
    private Integer masterPackingQty;

    @Column(name = "master_nest_code")
    private Integer masterNestCode;

    @Column(name = "inner_qty")
    private Integer innerQty;

    @Column(name = "pack_size")
    private Integer packSize;

    @Column(name = "nest_code")
    private String nestCode;

    @Column(name = "country_of_origin")
    private String countryOfOrigin;

    @Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "number_of_set")
    private Integer numberOfSet;

    @Column(name = "price_fob")
    private Float priceFOB;

    @Column(name = "qty_40_hc")
    private Integer qty40HC;

    @Column(name = "d_57_qty")
    private Integer d57Qty;

    @Column(name = "category")
    private String category;

    @Column(name = "labels")
    private String labels;

    @Column(name = "planning_notes")
    private String planningNotes;

    @Column(name = "fact_tag")
    private String factTag;

    @Column(name = "packaging_length")
    private Float packagingLength;

    @Column(name = "packaging_height")
    private Float packagingHeight;

    @Column(name = "packaging_width")
    private Float packagingWidth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "setIdProduct", "parentProduct", "material" }, allowSetters = true)
    private Product setIdProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "setIdProduct", "parentProduct", "material" }, allowSetters = true)
    private Product parentProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    private Material material;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public Product code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnit() {
        return this.unit;
    }

    public Product unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getWeight() {
        return this.weight;
    }

    public Product weight(Float weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getHeight() {
        return this.height;
    }

    public Product height(Float height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getWidth() {
        return this.width;
    }

    public Product width(Float width) {
        this.setWidth(width);
        return this;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getLength() {
        return this.length;
    }

    public Product length(Float length) {
        this.setLength(length);
        return this;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Product imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getType() {
        return this.type;
    }

    public Product type(Integer type) {
        this.setType(type);
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getColor() {
        return this.color;
    }

    public Product color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Float getCbm() {
        return this.cbm;
    }

    public Product cbm(Float cbm) {
        this.setCbm(cbm);
        return this;
    }

    public void setCbm(Float cbm) {
        this.cbm = cbm;
    }

    public Float getPrice() {
        return this.price;
    }

    public Product price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getConstruction() {
        return this.construction;
    }

    public Product construction(Integer construction) {
        this.setConstruction(construction);
        return this;
    }

    public void setConstruction(Integer construction) {
        this.construction = construction;
    }

    public Integer getMasterPackingQty() {
        return this.masterPackingQty;
    }

    public Product masterPackingQty(Integer masterPackingQty) {
        this.setMasterPackingQty(masterPackingQty);
        return this;
    }

    public void setMasterPackingQty(Integer masterPackingQty) {
        this.masterPackingQty = masterPackingQty;
    }

    public Integer getMasterNestCode() {
        return this.masterNestCode;
    }

    public Product masterNestCode(Integer masterNestCode) {
        this.setMasterNestCode(masterNestCode);
        return this;
    }

    public void setMasterNestCode(Integer masterNestCode) {
        this.masterNestCode = masterNestCode;
    }

    public Integer getInnerQty() {
        return this.innerQty;
    }

    public Product innerQty(Integer innerQty) {
        this.setInnerQty(innerQty);
        return this;
    }

    public void setInnerQty(Integer innerQty) {
        this.innerQty = innerQty;
    }

    public Integer getPackSize() {
        return this.packSize;
    }

    public Product packSize(Integer packSize) {
        this.setPackSize(packSize);
        return this;
    }

    public void setPackSize(Integer packSize) {
        this.packSize = packSize;
    }

    public String getNestCode() {
        return this.nestCode;
    }

    public Product nestCode(String nestCode) {
        this.setNestCode(nestCode);
        return this;
    }

    public void setNestCode(String nestCode) {
        this.nestCode = nestCode;
    }

    public String getCountryOfOrigin() {
        return this.countryOfOrigin;
    }

    public Product countryOfOrigin(String countryOfOrigin) {
        this.setCountryOfOrigin(countryOfOrigin);
        return this;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getVendorName() {
        return this.vendorName;
    }

    public Product vendorName(String vendorName) {
        this.setVendorName(vendorName);
        return this;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Integer getNumberOfSet() {
        return this.numberOfSet;
    }

    public Product numberOfSet(Integer numberOfSet) {
        this.setNumberOfSet(numberOfSet);
        return this;
    }

    public void setNumberOfSet(Integer numberOfSet) {
        this.numberOfSet = numberOfSet;
    }

    public Float getPriceFOB() {
        return this.priceFOB;
    }

    public Product priceFOB(Float priceFOB) {
        this.setPriceFOB(priceFOB);
        return this;
    }

    public void setPriceFOB(Float priceFOB) {
        this.priceFOB = priceFOB;
    }

    public Integer getQty40HC() {
        return this.qty40HC;
    }

    public Product qty40HC(Integer qty40HC) {
        this.setQty40HC(qty40HC);
        return this;
    }

    public void setQty40HC(Integer qty40HC) {
        this.qty40HC = qty40HC;
    }

    public Integer getd57Qty() {
        return this.d57Qty;
    }

    public Product d57Qty(Integer d57Qty) {
        this.setd57Qty(d57Qty);
        return this;
    }

    public void setd57Qty(Integer d57Qty) {
        this.d57Qty = d57Qty;
    }

    public String getCategory() {
        return this.category;
    }

    public Product category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLabels() {
        return this.labels;
    }

    public Product labels(String labels) {
        this.setLabels(labels);
        return this;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getPlanningNotes() {
        return this.planningNotes;
    }

    public Product planningNotes(String planningNotes) {
        this.setPlanningNotes(planningNotes);
        return this;
    }

    public void setPlanningNotes(String planningNotes) {
        this.planningNotes = planningNotes;
    }

    public String getFactTag() {
        return this.factTag;
    }

    public Product factTag(String factTag) {
        this.setFactTag(factTag);
        return this;
    }

    public void setFactTag(String factTag) {
        this.factTag = factTag;
    }

    public Float getPackagingLength() {
        return this.packagingLength;
    }

    public Product packagingLength(Float packagingLength) {
        this.setPackagingLength(packagingLength);
        return this;
    }

    public void setPackagingLength(Float packagingLength) {
        this.packagingLength = packagingLength;
    }

    public Float getPackagingHeight() {
        return this.packagingHeight;
    }

    public Product packagingHeight(Float packagingHeight) {
        this.setPackagingHeight(packagingHeight);
        return this;
    }

    public void setPackagingHeight(Float packagingHeight) {
        this.packagingHeight = packagingHeight;
    }

    public Float getPackagingWidth() {
        return this.packagingWidth;
    }

    public Product packagingWidth(Float packagingWidth) {
        this.setPackagingWidth(packagingWidth);
        return this;
    }

    public void setPackagingWidth(Float packagingWidth) {
        this.packagingWidth = packagingWidth;
    }

    public Product getSetIdProduct() {
        return this.setIdProduct;
    }

    public void setSetIdProduct(Product product) {
        this.setIdProduct = product;
    }

    public Product setIdProduct(Product product) {
        this.setSetIdProduct(product);
        return this;
    }

    public Product getParentProduct() {
        return this.parentProduct;
    }

    public void setParentProduct(Product product) {
        this.parentProduct = product;
    }

    public Product parentProduct(Product product) {
        this.setParentProduct(product);
        return this;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Product material(Material material) {
        this.setMaterial(material);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
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
            "}";
    }
}
