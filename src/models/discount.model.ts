import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';
import mongoose from 'mongoose';

@Schema({
  timestamps: true,
})
export class Discount extends Document {
  @Prop({ type: mongoose.Types.ObjectId, ref: 'Product', required: true })
  productId: mongoose.Schema.Types.ObjectId;

  @Prop({ type: Number, required: true })
  percentage: number;

  @Prop({ type: Date, required: true })
  startDate: Date;

  @Prop({ type: Date, required: true })
  endDate: Date;
}

export const DiscountSchema = SchemaFactory.createForClass(Discount);
