import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';
import mongoose from 'mongoose';

@Schema({
  timestamps: true,
})
export class Order extends Document {
  @Prop({ type: mongoose.Types.ObjectId, ref: 'User', required: true })
  user: mongoose.Schema.Types.ObjectId;

  @Prop({ type: mongoose.Types.ObjectId, ref: 'Cart', required: true })
  cart: mongoose.Schema.Types.ObjectId;

  @Prop({ type: Number, default: 0 })
  totalPrice: number;

  @Prop({
    type: String,
    enum: ['pending', 'completed', 'cancelled'],
    default: 'pending',
  })
  status: string;
}

export const OrderSchema = SchemaFactory.createForClass(Order);
