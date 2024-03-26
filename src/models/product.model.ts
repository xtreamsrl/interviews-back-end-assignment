import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';
import mongoose from 'mongoose';

@Schema({
  timestamps: true,
})
export class Product extends Document {
  @Prop({ type: String, required: true })
  name: string;

  @Prop({ type: String, required: false })
  description: string;

  @Prop({ type: String, required: false })
  image: string;

  @Prop({ type: Number, required: true })
  price: number;

  @Prop({ type: Number, required: true })
  availableQuantity: number;

  @Prop({ type: mongoose.Types.ObjectId, ref: 'Category', required: false })
  category: mongoose.Schema.Types.ObjectId | any;
}

export const ProductSchema = SchemaFactory.createForClass(Product);
