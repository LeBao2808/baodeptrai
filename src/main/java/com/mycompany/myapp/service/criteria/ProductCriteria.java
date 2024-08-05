package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Product} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter code;

    private StringFilter unit;

    private StringFilter description;

    private FloatFilter weight;

    private FloatFilter height;

    private FloatFilter width;

    private FloatFilter length;

    private StringFilter imageUrl;

    private IntegerFilter type;

    private StringFilter color;

    private FloatFilter cbm;

    private FloatFilter price;

    private IntegerFilter construction;

    private IntegerFilter masterPackingQty;

    private IntegerFilter masterNestCode;

    private IntegerFilter innerQty;

    private IntegerFilter packSize;

    private StringFilter nestCode;

    private StringFilter countryOfOrigin;

    private StringFilter vendorName;

    private IntegerFilter numberOfSet;

    private FloatFilter priceFOB;

    private IntegerFilter qty40HC;

    private IntegerFilter d57Qty;

    private StringFilter category;

    private StringFilter labels;

    private StringFilter planningNotes;

    private StringFilter factTag;

    private FloatFilter packagingLength;

    private FloatFilter packagingHeight;

    private FloatFilter packagingWidth;

    private LongFilter setIdProductId;

    private LongFilter parentProductId;

    private LongFilter materialId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.unit = other.optionalUnit().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.weight = other.optionalWeight().map(FloatFilter::copy).orElse(null);
        this.height = other.optionalHeight().map(FloatFilter::copy).orElse(null);
        this.width = other.optionalWidth().map(FloatFilter::copy).orElse(null);
        this.length = other.optionalLength().map(FloatFilter::copy).orElse(null);
        this.imageUrl = other.optionalImageUrl().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(IntegerFilter::copy).orElse(null);
        this.color = other.optionalColor().map(StringFilter::copy).orElse(null);
        this.cbm = other.optionalCbm().map(FloatFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(FloatFilter::copy).orElse(null);
        this.construction = other.optionalConstruction().map(IntegerFilter::copy).orElse(null);
        this.masterPackingQty = other.optionalMasterPackingQty().map(IntegerFilter::copy).orElse(null);
        this.masterNestCode = other.optionalMasterNestCode().map(IntegerFilter::copy).orElse(null);
        this.innerQty = other.optionalInnerQty().map(IntegerFilter::copy).orElse(null);
        this.packSize = other.optionalPackSize().map(IntegerFilter::copy).orElse(null);
        this.nestCode = other.optionalNestCode().map(StringFilter::copy).orElse(null);
        this.countryOfOrigin = other.optionalCountryOfOrigin().map(StringFilter::copy).orElse(null);
        this.vendorName = other.optionalVendorName().map(StringFilter::copy).orElse(null);
        this.numberOfSet = other.optionalNumberOfSet().map(IntegerFilter::copy).orElse(null);
        this.priceFOB = other.optionalPriceFOB().map(FloatFilter::copy).orElse(null);
        this.qty40HC = other.optionalQty40HC().map(IntegerFilter::copy).orElse(null);
        this.d57Qty = other.optionald57Qty().map(IntegerFilter::copy).orElse(null);
        this.category = other.optionalCategory().map(StringFilter::copy).orElse(null);
        this.labels = other.optionalLabels().map(StringFilter::copy).orElse(null);
        this.planningNotes = other.optionalPlanningNotes().map(StringFilter::copy).orElse(null);
        this.factTag = other.optionalFactTag().map(StringFilter::copy).orElse(null);
        this.packagingLength = other.optionalPackagingLength().map(FloatFilter::copy).orElse(null);
        this.packagingHeight = other.optionalPackagingHeight().map(FloatFilter::copy).orElse(null);
        this.packagingWidth = other.optionalPackagingWidth().map(FloatFilter::copy).orElse(null);
        this.setIdProductId = other.optionalSetIdProductId().map(LongFilter::copy).orElse(null);
        this.parentProductId = other.optionalParentProductId().map(LongFilter::copy).orElse(null);
        this.materialId = other.optionalMaterialId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getUnit() {
        return unit;
    }

    public Optional<StringFilter> optionalUnit() {
        return Optional.ofNullable(unit);
    }

    public StringFilter unit() {
        if (unit == null) {
            setUnit(new StringFilter());
        }
        return unit;
    }

    public void setUnit(StringFilter unit) {
        this.unit = unit;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public FloatFilter getWeight() {
        return weight;
    }

    public Optional<FloatFilter> optionalWeight() {
        return Optional.ofNullable(weight);
    }

    public FloatFilter weight() {
        if (weight == null) {
            setWeight(new FloatFilter());
        }
        return weight;
    }

    public void setWeight(FloatFilter weight) {
        this.weight = weight;
    }

    public FloatFilter getHeight() {
        return height;
    }

    public Optional<FloatFilter> optionalHeight() {
        return Optional.ofNullable(height);
    }

    public FloatFilter height() {
        if (height == null) {
            setHeight(new FloatFilter());
        }
        return height;
    }

    public void setHeight(FloatFilter height) {
        this.height = height;
    }

    public FloatFilter getWidth() {
        return width;
    }

    public Optional<FloatFilter> optionalWidth() {
        return Optional.ofNullable(width);
    }

    public FloatFilter width() {
        if (width == null) {
            setWidth(new FloatFilter());
        }
        return width;
    }

    public void setWidth(FloatFilter width) {
        this.width = width;
    }

    public FloatFilter getLength() {
        return length;
    }

    public Optional<FloatFilter> optionalLength() {
        return Optional.ofNullable(length);
    }

    public FloatFilter length() {
        if (length == null) {
            setLength(new FloatFilter());
        }
        return length;
    }

    public void setLength(FloatFilter length) {
        this.length = length;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public Optional<StringFilter> optionalImageUrl() {
        return Optional.ofNullable(imageUrl);
    }

    public StringFilter imageUrl() {
        if (imageUrl == null) {
            setImageUrl(new StringFilter());
        }
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public IntegerFilter getType() {
        return type;
    }

    public Optional<IntegerFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public IntegerFilter type() {
        if (type == null) {
            setType(new IntegerFilter());
        }
        return type;
    }

    public void setType(IntegerFilter type) {
        this.type = type;
    }

    public StringFilter getColor() {
        return color;
    }

    public Optional<StringFilter> optionalColor() {
        return Optional.ofNullable(color);
    }

    public StringFilter color() {
        if (color == null) {
            setColor(new StringFilter());
        }
        return color;
    }

    public void setColor(StringFilter color) {
        this.color = color;
    }

    public FloatFilter getCbm() {
        return cbm;
    }

    public Optional<FloatFilter> optionalCbm() {
        return Optional.ofNullable(cbm);
    }

    public FloatFilter cbm() {
        if (cbm == null) {
            setCbm(new FloatFilter());
        }
        return cbm;
    }

    public void setCbm(FloatFilter cbm) {
        this.cbm = cbm;
    }

    public FloatFilter getPrice() {
        return price;
    }

    public Optional<FloatFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public FloatFilter price() {
        if (price == null) {
            setPrice(new FloatFilter());
        }
        return price;
    }

    public void setPrice(FloatFilter price) {
        this.price = price;
    }

    public IntegerFilter getConstruction() {
        return construction;
    }

    public Optional<IntegerFilter> optionalConstruction() {
        return Optional.ofNullable(construction);
    }

    public IntegerFilter construction() {
        if (construction == null) {
            setConstruction(new IntegerFilter());
        }
        return construction;
    }

    public void setConstruction(IntegerFilter construction) {
        this.construction = construction;
    }

    public IntegerFilter getMasterPackingQty() {
        return masterPackingQty;
    }

    public Optional<IntegerFilter> optionalMasterPackingQty() {
        return Optional.ofNullable(masterPackingQty);
    }

    public IntegerFilter masterPackingQty() {
        if (masterPackingQty == null) {
            setMasterPackingQty(new IntegerFilter());
        }
        return masterPackingQty;
    }

    public void setMasterPackingQty(IntegerFilter masterPackingQty) {
        this.masterPackingQty = masterPackingQty;
    }

    public IntegerFilter getMasterNestCode() {
        return masterNestCode;
    }

    public Optional<IntegerFilter> optionalMasterNestCode() {
        return Optional.ofNullable(masterNestCode);
    }

    public IntegerFilter masterNestCode() {
        if (masterNestCode == null) {
            setMasterNestCode(new IntegerFilter());
        }
        return masterNestCode;
    }

    public void setMasterNestCode(IntegerFilter masterNestCode) {
        this.masterNestCode = masterNestCode;
    }

    public IntegerFilter getInnerQty() {
        return innerQty;
    }

    public Optional<IntegerFilter> optionalInnerQty() {
        return Optional.ofNullable(innerQty);
    }

    public IntegerFilter innerQty() {
        if (innerQty == null) {
            setInnerQty(new IntegerFilter());
        }
        return innerQty;
    }

    public void setInnerQty(IntegerFilter innerQty) {
        this.innerQty = innerQty;
    }

    public IntegerFilter getPackSize() {
        return packSize;
    }

    public Optional<IntegerFilter> optionalPackSize() {
        return Optional.ofNullable(packSize);
    }

    public IntegerFilter packSize() {
        if (packSize == null) {
            setPackSize(new IntegerFilter());
        }
        return packSize;
    }

    public void setPackSize(IntegerFilter packSize) {
        this.packSize = packSize;
    }

    public StringFilter getNestCode() {
        return nestCode;
    }

    public Optional<StringFilter> optionalNestCode() {
        return Optional.ofNullable(nestCode);
    }

    public StringFilter nestCode() {
        if (nestCode == null) {
            setNestCode(new StringFilter());
        }
        return nestCode;
    }

    public void setNestCode(StringFilter nestCode) {
        this.nestCode = nestCode;
    }

    public StringFilter getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public Optional<StringFilter> optionalCountryOfOrigin() {
        return Optional.ofNullable(countryOfOrigin);
    }

    public StringFilter countryOfOrigin() {
        if (countryOfOrigin == null) {
            setCountryOfOrigin(new StringFilter());
        }
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(StringFilter countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public StringFilter getVendorName() {
        return vendorName;
    }

    public Optional<StringFilter> optionalVendorName() {
        return Optional.ofNullable(vendorName);
    }

    public StringFilter vendorName() {
        if (vendorName == null) {
            setVendorName(new StringFilter());
        }
        return vendorName;
    }

    public void setVendorName(StringFilter vendorName) {
        this.vendorName = vendorName;
    }

    public IntegerFilter getNumberOfSet() {
        return numberOfSet;
    }

    public Optional<IntegerFilter> optionalNumberOfSet() {
        return Optional.ofNullable(numberOfSet);
    }

    public IntegerFilter numberOfSet() {
        if (numberOfSet == null) {
            setNumberOfSet(new IntegerFilter());
        }
        return numberOfSet;
    }

    public void setNumberOfSet(IntegerFilter numberOfSet) {
        this.numberOfSet = numberOfSet;
    }

    public FloatFilter getPriceFOB() {
        return priceFOB;
    }

    public Optional<FloatFilter> optionalPriceFOB() {
        return Optional.ofNullable(priceFOB);
    }

    public FloatFilter priceFOB() {
        if (priceFOB == null) {
            setPriceFOB(new FloatFilter());
        }
        return priceFOB;
    }

    public void setPriceFOB(FloatFilter priceFOB) {
        this.priceFOB = priceFOB;
    }

    public IntegerFilter getQty40HC() {
        return qty40HC;
    }

    public Optional<IntegerFilter> optionalQty40HC() {
        return Optional.ofNullable(qty40HC);
    }

    public IntegerFilter qty40HC() {
        if (qty40HC == null) {
            setQty40HC(new IntegerFilter());
        }
        return qty40HC;
    }

    public void setQty40HC(IntegerFilter qty40HC) {
        this.qty40HC = qty40HC;
    }

    public IntegerFilter getd57Qty() {
        return d57Qty;
    }

    public Optional<IntegerFilter> optionald57Qty() {
        return Optional.ofNullable(d57Qty);
    }

    public IntegerFilter d57Qty() {
        if (d57Qty == null) {
            setd57Qty(new IntegerFilter());
        }
        return d57Qty;
    }

    public void setd57Qty(IntegerFilter d57Qty) {
        this.d57Qty = d57Qty;
    }

    public StringFilter getCategory() {
        return category;
    }

    public Optional<StringFilter> optionalCategory() {
        return Optional.ofNullable(category);
    }

    public StringFilter category() {
        if (category == null) {
            setCategory(new StringFilter());
        }
        return category;
    }

    public void setCategory(StringFilter category) {
        this.category = category;
    }

    public StringFilter getLabels() {
        return labels;
    }

    public Optional<StringFilter> optionalLabels() {
        return Optional.ofNullable(labels);
    }

    public StringFilter labels() {
        if (labels == null) {
            setLabels(new StringFilter());
        }
        return labels;
    }

    public void setLabels(StringFilter labels) {
        this.labels = labels;
    }

    public StringFilter getPlanningNotes() {
        return planningNotes;
    }

    public Optional<StringFilter> optionalPlanningNotes() {
        return Optional.ofNullable(planningNotes);
    }

    public StringFilter planningNotes() {
        if (planningNotes == null) {
            setPlanningNotes(new StringFilter());
        }
        return planningNotes;
    }

    public void setPlanningNotes(StringFilter planningNotes) {
        this.planningNotes = planningNotes;
    }

    public StringFilter getFactTag() {
        return factTag;
    }

    public Optional<StringFilter> optionalFactTag() {
        return Optional.ofNullable(factTag);
    }

    public StringFilter factTag() {
        if (factTag == null) {
            setFactTag(new StringFilter());
        }
        return factTag;
    }

    public void setFactTag(StringFilter factTag) {
        this.factTag = factTag;
    }

    public FloatFilter getPackagingLength() {
        return packagingLength;
    }

    public Optional<FloatFilter> optionalPackagingLength() {
        return Optional.ofNullable(packagingLength);
    }

    public FloatFilter packagingLength() {
        if (packagingLength == null) {
            setPackagingLength(new FloatFilter());
        }
        return packagingLength;
    }

    public void setPackagingLength(FloatFilter packagingLength) {
        this.packagingLength = packagingLength;
    }

    public FloatFilter getPackagingHeight() {
        return packagingHeight;
    }

    public Optional<FloatFilter> optionalPackagingHeight() {
        return Optional.ofNullable(packagingHeight);
    }

    public FloatFilter packagingHeight() {
        if (packagingHeight == null) {
            setPackagingHeight(new FloatFilter());
        }
        return packagingHeight;
    }

    public void setPackagingHeight(FloatFilter packagingHeight) {
        this.packagingHeight = packagingHeight;
    }

    public FloatFilter getPackagingWidth() {
        return packagingWidth;
    }

    public Optional<FloatFilter> optionalPackagingWidth() {
        return Optional.ofNullable(packagingWidth);
    }

    public FloatFilter packagingWidth() {
        if (packagingWidth == null) {
            setPackagingWidth(new FloatFilter());
        }
        return packagingWidth;
    }

    public void setPackagingWidth(FloatFilter packagingWidth) {
        this.packagingWidth = packagingWidth;
    }

    public LongFilter getSetIdProductId() {
        return setIdProductId;
    }

    public Optional<LongFilter> optionalSetIdProductId() {
        return Optional.ofNullable(setIdProductId);
    }

    public LongFilter setIdProductId() {
        if (setIdProductId == null) {
            setSetIdProductId(new LongFilter());
        }
        return setIdProductId;
    }

    public void setSetIdProductId(LongFilter setIdProductId) {
        this.setIdProductId = setIdProductId;
    }

    public LongFilter getParentProductId() {
        return parentProductId;
    }

    public Optional<LongFilter> optionalParentProductId() {
        return Optional.ofNullable(parentProductId);
    }

    public LongFilter parentProductId() {
        if (parentProductId == null) {
            setParentProductId(new LongFilter());
        }
        return parentProductId;
    }

    public void setParentProductId(LongFilter parentProductId) {
        this.parentProductId = parentProductId;
    }

    public LongFilter getMaterialId() {
        return materialId;
    }

    public Optional<LongFilter> optionalMaterialId() {
        return Optional.ofNullable(materialId);
    }

    public LongFilter materialId() {
        if (materialId == null) {
            setMaterialId(new LongFilter());
        }
        return materialId;
    }

    public void setMaterialId(LongFilter materialId) {
        this.materialId = materialId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(unit, that.unit) &&
            Objects.equals(description, that.description) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(height, that.height) &&
            Objects.equals(width, that.width) &&
            Objects.equals(length, that.length) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(type, that.type) &&
            Objects.equals(color, that.color) &&
            Objects.equals(cbm, that.cbm) &&
            Objects.equals(price, that.price) &&
            Objects.equals(construction, that.construction) &&
            Objects.equals(masterPackingQty, that.masterPackingQty) &&
            Objects.equals(masterNestCode, that.masterNestCode) &&
            Objects.equals(innerQty, that.innerQty) &&
            Objects.equals(packSize, that.packSize) &&
            Objects.equals(nestCode, that.nestCode) &&
            Objects.equals(countryOfOrigin, that.countryOfOrigin) &&
            Objects.equals(vendorName, that.vendorName) &&
            Objects.equals(numberOfSet, that.numberOfSet) &&
            Objects.equals(priceFOB, that.priceFOB) &&
            Objects.equals(qty40HC, that.qty40HC) &&
            Objects.equals(d57Qty, that.d57Qty) &&
            Objects.equals(category, that.category) &&
            Objects.equals(labels, that.labels) &&
            Objects.equals(planningNotes, that.planningNotes) &&
            Objects.equals(factTag, that.factTag) &&
            Objects.equals(packagingLength, that.packagingLength) &&
            Objects.equals(packagingHeight, that.packagingHeight) &&
            Objects.equals(packagingWidth, that.packagingWidth) &&
            Objects.equals(setIdProductId, that.setIdProductId) &&
            Objects.equals(parentProductId, that.parentProductId) &&
            Objects.equals(materialId, that.materialId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            code,
            unit,
            description,
            weight,
            height,
            width,
            length,
            imageUrl,
            type,
            color,
            cbm,
            price,
            construction,
            masterPackingQty,
            masterNestCode,
            innerQty,
            packSize,
            nestCode,
            countryOfOrigin,
            vendorName,
            numberOfSet,
            priceFOB,
            qty40HC,
            d57Qty,
            category,
            labels,
            planningNotes,
            factTag,
            packagingLength,
            packagingHeight,
            packagingWidth,
            setIdProductId,
            parentProductId,
            materialId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalUnit().map(f -> "unit=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalWeight().map(f -> "weight=" + f + ", ").orElse("") +
            optionalHeight().map(f -> "height=" + f + ", ").orElse("") +
            optionalWidth().map(f -> "width=" + f + ", ").orElse("") +
            optionalLength().map(f -> "length=" + f + ", ").orElse("") +
            optionalImageUrl().map(f -> "imageUrl=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalColor().map(f -> "color=" + f + ", ").orElse("") +
            optionalCbm().map(f -> "cbm=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalConstruction().map(f -> "construction=" + f + ", ").orElse("") +
            optionalMasterPackingQty().map(f -> "masterPackingQty=" + f + ", ").orElse("") +
            optionalMasterNestCode().map(f -> "masterNestCode=" + f + ", ").orElse("") +
            optionalInnerQty().map(f -> "innerQty=" + f + ", ").orElse("") +
            optionalPackSize().map(f -> "packSize=" + f + ", ").orElse("") +
            optionalNestCode().map(f -> "nestCode=" + f + ", ").orElse("") +
            optionalCountryOfOrigin().map(f -> "countryOfOrigin=" + f + ", ").orElse("") +
            optionalVendorName().map(f -> "vendorName=" + f + ", ").orElse("") +
            optionalNumberOfSet().map(f -> "numberOfSet=" + f + ", ").orElse("") +
            optionalPriceFOB().map(f -> "priceFOB=" + f + ", ").orElse("") +
            optionalQty40HC().map(f -> "qty40HC=" + f + ", ").orElse("") +
            optionald57Qty().map(f -> "d57Qty=" + f + ", ").orElse("") +
            optionalCategory().map(f -> "category=" + f + ", ").orElse("") +
            optionalLabels().map(f -> "labels=" + f + ", ").orElse("") +
            optionalPlanningNotes().map(f -> "planningNotes=" + f + ", ").orElse("") +
            optionalFactTag().map(f -> "factTag=" + f + ", ").orElse("") +
            optionalPackagingLength().map(f -> "packagingLength=" + f + ", ").orElse("") +
            optionalPackagingHeight().map(f -> "packagingHeight=" + f + ", ").orElse("") +
            optionalPackagingWidth().map(f -> "packagingWidth=" + f + ", ").orElse("") +
            optionalSetIdProductId().map(f -> "setIdProductId=" + f + ", ").orElse("") +
            optionalParentProductId().map(f -> "parentProductId=" + f + ", ").orElse("") +
            optionalMaterialId().map(f -> "materialId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
