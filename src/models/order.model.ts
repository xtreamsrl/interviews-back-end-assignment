import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';
import mongoose from 'mongoose';

@Schema({
  timestamps: true,
})
export class Order extends Document {
  @Prop({ type: String, required: true })
  user_id: string;

  @Prop({
    type: [{ product_id: mongoose.Types.ObjectId, quantity: Number }],
    required: true,
  })
  products: { product_id: mongoose.Types.ObjectId; quantity: number }[];

  @Prop({ type: Number, required: true, default: 0 })
  total_price: number;

  @Prop({
    type: String,
    enum: ['pending', 'completed', 'cancelled'],
    default: 'pending',
  })
  status: string;
}

export const OrderSchema = SchemaFactory.createForClass(Order);
