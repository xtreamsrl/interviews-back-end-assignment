import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';
import * as mongoose from 'mongoose';
import { Product } from './product.model';

export type CartDocument = Cart & Document;

@Schema({
  timestamps: true,
})
export class Cart {
  @Prop({ required: true, type: String })
  name: string;

  @Prop({ type: mongoose.Types.ObjectId, ref: 'User', required: true })
  user: mongoose.Schema.Types.ObjectId;

  @Prop({
    type: [
      {
        product: { type: mongoose.Schema.Types.ObjectId, ref: 'Product' },
        quantity: Number,
      },
    ],
    required: true,
  })
  cartItems: { product: Product; quantity: number }[];

  @Prop({ required: false })
  total: number;
}

export const CartSchema = SchemaFactory.createForClass(Cart);
